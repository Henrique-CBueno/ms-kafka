package com.henrique.icompras.pedidos.controller;

import com.henrique.icompras.pedidos.controller.dto.AdicionarNovoPagamentoDTO;
import com.henrique.icompras.pedidos.controller.dto.NovoPedidoDTO;
import com.henrique.icompras.pedidos.controller.mappers.PedidoMapper;
import com.henrique.icompras.pedidos.model.ErroResposta;
import com.henrique.icompras.pedidos.model.Pedido;
import com.henrique.icompras.pedidos.model.exeption.ItemNaoEncontradoException;
import com.henrique.icompras.pedidos.model.exeption.ValidationExeption;
import com.henrique.icompras.pedidos.publisher.DetalhePedidoMapper;
import com.henrique.icompras.pedidos.publisher.representation.DetalhePedidoRepresentation;
import com.henrique.icompras.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("pedidos")
@RequiredArgsConstructor
public class PedidoController {

    private final PedidoService pedidoService;
    private final PedidoMapper pedidoMapper;
    private final DetalhePedidoMapper detalhePedidoMapper;

    @PostMapping
    public ResponseEntity<Object> criar(@RequestBody NovoPedidoDTO novoPedidoDTO) {
        try {

            Pedido pedido = pedidoMapper.map(novoPedidoDTO);
            Pedido novoPedido = pedidoService.criarPedido(pedido);
            return ResponseEntity.ok(novoPedido.getCodigo());

        } catch (ValidationExeption e) {

            ErroResposta erro = new ErroResposta("Erro validação", e.getField(), e.getMessage());
            return ResponseEntity.badRequest().body(erro);

        }
    }

    @PostMapping("{codigoPedido}/pagamentos")
    public ResponseEntity<Object> adicionarNovoPagamento(@RequestBody AdicionarNovoPagamentoDTO adicionarNovoPagamentoDTO,
                                                         @PathVariable Long codigoPedido) {

        try {

            pedidoService.adicionarNovoPagamento(codigoPedido,
                                                adicionarNovoPagamentoDTO.dados(),
                                                adicionarNovoPagamentoDTO.tipoPagamento());
            return ResponseEntity.noContent().build();

        } catch (ItemNaoEncontradoException e) {
            ErroResposta erro = new ErroResposta("Pedido não encontrado", "codigoPedido", e.getMessage());
            return ResponseEntity.badRequest().body(erro);
        }

    }

    @GetMapping("{codigoPedido}")
    public ResponseEntity<DetalhePedidoRepresentation> obterDetalhesPedido(@PathVariable Long codigoPedido) {
        return pedidoService
                .carregarDadosCompletosPedido(codigoPedido)
                .map(detalhePedidoMapper::map)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
