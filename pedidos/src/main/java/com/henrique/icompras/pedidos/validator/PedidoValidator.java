package com.henrique.icompras.pedidos.validator;

import com.henrique.icompras.pedidos.client.ClientesClient;
import com.henrique.icompras.pedidos.client.ProdutosClient;
import com.henrique.icompras.pedidos.client.representation.ClienteRepresentation;
import com.henrique.icompras.pedidos.client.representation.ProdutoRepresentation;
import com.henrique.icompras.pedidos.model.ItemPedido;
import com.henrique.icompras.pedidos.model.Pedido;
import com.henrique.icompras.pedidos.model.exeption.ValidationExeption;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoValidator {

    private final ProdutosClient produtosClient;
    private final ClientesClient clientesClient;

    public void validar(Pedido pedido) {

        Long codigoCliente = pedido.getCodigoCliente();

        validarCliente(codigoCliente);

        pedido.getItens()
                .forEach(this::validarItem);

    }


    private void validarCliente(Long codigoCliente) {

        try {

            ResponseEntity<ClienteRepresentation> response = clientesClient.obterDados(codigoCliente);
            ClienteRepresentation cliente = response.getBody();
            log.info(
                    "Cliente de codigo {} encontrado: {}!",
                    cliente != null ? cliente.codigo() : "erro ao buscar o codigo do cliente",
                    cliente != null ? cliente.nome() : "erro ao buscar o nome do cliente"
            );

            if (!cliente.ativo()) {
                throw new ValidationExeption("codigoCliente","CLIENTE " + cliente.codigo() + " INATIVO");
            }

        } catch (FeignException.NotFound e) {

            log.error("CLIENTE NAO ENCONTRADO!");
            String message = String.format("Cliente de código %d não encontrado!", codigoCliente);
            throw new ValidationExeption("codigoCliente", message);

        }

    }

    private void validarItem(ItemPedido item) {

        try {

            ResponseEntity<ProdutoRepresentation> response = produtosClient.obterDados(item.getCodigoProduto());
            ProdutoRepresentation produto = response.getBody();
            log.info(
                    "Produto de codigo {} encontrado: {}!",
                    produto != null ? produto.codigo() : "erro ao buscar o codigo do produto",
                    produto != null ? produto.nome() : "erro ao buscar o nome do produto"
            );

            if (!produto.ativo()) {
                throw new ValidationExeption("codigoProduto","PRODUTO " + produto.codigo() + " INATIVO");
            }

        } catch (FeignException.NotFound e) {

            log.error("PRODUTO NAO ENCONTRADO!");
            String message = String.format("Produto de código %d não encontrado!", item.getCodigoProduto());
            throw new ValidationExeption("codigoProduto", message);

        }

    }
}
