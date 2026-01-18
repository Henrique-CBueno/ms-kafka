package com.henrique.icompras.pedidos.repository;

import com.henrique.icompras.pedidos.model.ItemPedido;
import com.henrique.icompras.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ItemPedidoRepository extends JpaRepository<ItemPedido, Long> {
    List<ItemPedido> findByPedido(Pedido pedido);
}
