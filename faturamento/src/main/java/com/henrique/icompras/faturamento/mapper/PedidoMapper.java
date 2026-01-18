package com.henrique.icompras.faturamento.mapper;

import com.henrique.icompras.faturamento.model.Cliente;
import com.henrique.icompras.faturamento.model.ItemPedido;
import com.henrique.icompras.faturamento.model.Pedido;
import com.henrique.icompras.faturamento.subscriber.representation.DetalheItemPedidoRepresentation;
import com.henrique.icompras.faturamento.subscriber.representation.DetalhePedidoRepresentation;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class PedidoMapper {

    public Pedido map(DetalhePedidoRepresentation representation) {
        Cliente cliente = new Cliente(
              representation.nome(),
              representation.cpf(),
              representation.logradouro(),
              representation.numero(),
              representation.bairro(),
              representation.email(),
              representation.telefone()
        );

        List<ItemPedido> itens = representation.itens().stream().map(this::mapItem).collect(Collectors.toList());

        return new Pedido(representation.codigo(),
                cliente,
                representation.dataPedido(),
                representation.total(),
                itens);
    }

    private ItemPedido mapItem(DetalheItemPedidoRepresentation representation) {
        return new ItemPedido(representation.codigoProduto(),
                representation.nomeProduto(),
                representation.valorUnitario(),
                representation.quantidade(),
                representation.total());
    }
}
