package com.henrique.icompras.faturamento.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
public class ItemPedido {

    // CLASSE POIS O JASPEREPORTS OBRIGA, PODERIA SER RECORD

    private Long codigo;
    private String nome;
    private BigDecimal valorUnitario;
    private Integer quantidade;
    private BigDecimal total;

}
