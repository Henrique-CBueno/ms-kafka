package com.henrique.icompras.pedidos.controller.mappers;

import com.henrique.icompras.pedidos.controller.dto.ItemPedidoDTO;
import com.henrique.icompras.pedidos.model.ItemPedido;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ItemPedidoMapper {

    ItemPedido map(ItemPedidoDTO dto);
}
