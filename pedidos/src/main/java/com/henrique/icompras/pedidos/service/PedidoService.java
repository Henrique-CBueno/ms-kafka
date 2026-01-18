package com.henrique.icompras.pedidos.service;

import com.henrique.icompras.pedidos.client.ClientesClient;
import com.henrique.icompras.pedidos.client.ProdutosClient;
import com.henrique.icompras.pedidos.client.ServicoBancarioClient;
import com.henrique.icompras.pedidos.client.representation.ClienteRepresentation;
import com.henrique.icompras.pedidos.client.representation.ProdutoRepresentation;
import com.henrique.icompras.pedidos.exceptions.PedidoNaoEncontradoException;
import com.henrique.icompras.pedidos.model.DadosPagamento;
import com.henrique.icompras.pedidos.model.ItemPedido;
import com.henrique.icompras.pedidos.model.Pedido;
import com.henrique.icompras.pedidos.model.enums.StatusPedido;
import com.henrique.icompras.pedidos.model.enums.TipoPagamento;
import com.henrique.icompras.pedidos.model.exeption.ItemNaoEncontradoException;
import com.henrique.icompras.pedidos.publisher.PagamentoPublisher;
import com.henrique.icompras.pedidos.repository.ItemPedidoRepository;
import com.henrique.icompras.pedidos.repository.PedidoRepository;
import com.henrique.icompras.pedidos.validator.PedidoValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final ItemPedidoRepository itemPedidoRepository;
    private final PedidoValidator validator;
    private final ServicoBancarioClient servicoBancarioClient;
    private final ClientesClient clientesClient;
    private final ProdutosClient produtosClient;
    private final PagamentoPublisher pagamentoPublisher;

    @Transactional
    public Pedido criarPedido(Pedido pedido) {
        validator.validar(pedido);

        pedidoRepository.saveAndFlush(pedido);
        itemPedidoRepository.saveAllAndFlush(pedido.getItens());

        String chavePagamento = servicoBancarioClient.solicitarPagamento(pedido);

        pedido.setChavePagamento(chavePagamento);

        return pedido;
    }

    public void atualizarStatusPagamento(Long codigoPedido, String chavePagamento, boolean sucesso, String observacoes)
            throws PedidoNaoEncontradoException {

        Optional<Pedido> pedidoEncontrado = pedidoRepository.findByCodigoAndChavePagamento(codigoPedido,
                chavePagamento);

        if (pedidoEncontrado.isEmpty()) {
            String msg = String.format("Pedido de codigo %d e chave de pagamento %s não encontrado!",
                    codigoPedido,
                    chavePagamento);

            log.error(msg);
            throw new PedidoNaoEncontradoException(
                    String.format("Pedido de codigo %d e chave de pagamento %s não encontrado!",
                            codigoPedido,
                            chavePagamento));
        }

        Pedido pedido = pedidoEncontrado.get();

        if (sucesso) {
            prepararEPublicarPedidoPago(pedido);
        } else {
            pedido.setStatus(StatusPedido.ERRO_PAGAMENTO);
            pedido.setObservacoes(observacoes);
        }

        pedidoRepository.saveAndFlush(pedido);

    }

    private void prepararEPublicarPedidoPago(Pedido pedido) {
        pedido.setStatus(StatusPedido.PAGO);
        carregarDadosCliente(pedido);
        carregarItemsPedido(pedido);
        pagamentoPublisher.publicar(pedido);
    }

    @Transactional
    public void adicionarNovoPagamento(Long codigoPedido, String dadosCartao, TipoPagamento tipoPagamento) {

        Optional<Pedido> pedidoEncontrado = pedidoRepository.findById(codigoPedido);

        if (pedidoEncontrado.isEmpty()) {
            throw new ItemNaoEncontradoException("Pedido de codigo " + codigoPedido + "  não encontrado!");
        }

        Pedido pedido = pedidoEncontrado.get();

        DadosPagamento dadosPagamento = new DadosPagamento();
        dadosPagamento.setTipoPagamento(tipoPagamento);
        dadosPagamento.setDados(dadosCartao);

        pedido.setDadosPagamento(dadosPagamento);
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setObservacoes("Novo pagamento criado");

        String novaChavePagamento = servicoBancarioClient.solicitarPagamento(pedido);
        pedido.setChavePagamento(novaChavePagamento);

        pedidoRepository.saveAndFlush(pedido);
    }

    public Optional<Pedido> carregarDadosCompletosPedido(Long codigo) {
        Optional<Pedido> pedido = pedidoRepository.findById(codigo);
        pedido.ifPresent(this::carregarDadosCliente);
        pedido.ifPresent(this::carregarItemsPedido);
        return pedido;
    }

    private void carregarDadosCliente(Pedido pedido) {
        Long codigoCliente = pedido.getCodigoCliente();
        ResponseEntity<ClienteRepresentation> response = clientesClient.obterDados(codigoCliente);
        pedido.setDadosCliente(response.getBody());
    }

    private void carregarItemsPedido(Pedido pedido) {
        List<ItemPedido> itens = itemPedidoRepository.findByPedido(pedido);
        pedido.setItens(itens);
        pedido.getItens().forEach(this::carregarDadosProduto);
    }

    private void carregarDadosProduto(ItemPedido item) {
        Long codigoProduto = item.getCodigoProduto();
        ResponseEntity<ProdutoRepresentation> response = produtosClient.obterDados(codigoProduto);
        item.setNomeProduto(response.getBody().nome());
    }
}
