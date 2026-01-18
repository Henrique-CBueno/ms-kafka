package com.henrique.icompras.pedidos.repository;

import com.henrique.icompras.pedidos.model.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Long> {
    Optional<Pedido> findByCodigoAndChavePagamento(Long codigo, String chavePagamento);
}
