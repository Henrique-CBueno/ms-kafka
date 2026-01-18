package com.henrique.icompras.pedidos.model;

import com.henrique.icompras.pedidos.client.representation.ClienteRepresentation;
import com.henrique.icompras.pedidos.controller.dto.DadosPagamentoDTO;
import com.henrique.icompras.pedidos.model.enums.StatusPedido;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "pedido")
public class Pedido {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long codigo;

    @Column(name = "codigo_cliente", nullable = false)
    private Long codigoCliente;

    @Column(name = "data_pedido", nullable = false)
    private Instant dataPedido;

    @Column(name = "chave_pagamento")
    private String chavePagamento;

    @Column(name = "observacoes")
    private String observacoes;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private StatusPedido status;

    @Column(name = "total", nullable = false, precision = 16, scale = 2)
    private BigDecimal total;

    @Column(name = "codigo_rastreio")
    private String codigoRastreio;

    @Column(name = "url_nf")
    private String urlNotaFiscal;


    // transient é quando nao é do banco
    @Transient
    private DadosPagamento dadosPagamento;

    @Transient
    private ClienteRepresentation dadosCliente;

    @OneToMany(mappedBy = "pedido")
    private List<ItemPedido> itens;

}