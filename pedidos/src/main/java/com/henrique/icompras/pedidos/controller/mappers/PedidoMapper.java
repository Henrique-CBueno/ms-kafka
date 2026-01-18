package com.henrique.icompras.pedidos.controller.mappers;

import com.henrique.icompras.pedidos.client.ProdutosClient;
import com.henrique.icompras.pedidos.controller.dto.ItemPedidoDTO;
import com.henrique.icompras.pedidos.controller.dto.NovoPedidoDTO;
import com.henrique.icompras.pedidos.model.ItemPedido;
import com.henrique.icompras.pedidos.model.Pedido;
import com.henrique.icompras.pedidos.model.enums.StatusPedido;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface PedidoMapper {

    ItemPedidoMapper ITEM_PEDIDO_MAPPER = Mappers.getMapper(ItemPedidoMapper.class);

    @Mapping(source = "itens", target = "itens", qualifiedByName = "mapItens")
    @Mapping(source = "dadosPagamento", target = "dadosPagamento")
    Pedido map(NovoPedidoDTO dto);

    @Named("mapItens")
    default List<ItemPedido> mapItens(List<ItemPedidoDTO> dtos) {
        return dtos
                .stream()
                .map(ITEM_PEDIDO_MAPPER::map)
                .collect(Collectors.toList());
    }

    @AfterMapping
    default void afterMapping(@MappingTarget Pedido pedido) {
        pedido.setStatus(StatusPedido.REALIZADO);
        pedido.setDataPedido(Instant.now());


        BigDecimal total = getTotal(pedido);

        pedido.setTotal(total);

        pedido.getItens().forEach(item -> item.setPedido(pedido));
    }

    private void setValoresDosItens(Pedido pedido,
                                    @Context ProdutosClient produtosClient) {
        pedido.getItens().forEach(
                item -> {
                    produtosClient.obterDados(item.getCodigoProduto());
                }
        );
    }


    private static BigDecimal getTotal(Pedido pedido) {
        return pedido.getItens().stream().map(
                item -> item.getValorUnitario().multiply(BigDecimal.valueOf(item.getQuantidade()))
        ).reduce(BigDecimal.ZERO, BigDecimal::add).abs();
    }
}


