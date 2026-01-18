package com.henrique.icompras.faturamento.subscriber.representation;

import java.math.BigDecimal;

public record DetalheItemPedidoRepresentation(Long codigoProduto,
                                              String nomeProduto,
                                              Integer quantidade,
                                              BigDecimal valorUnitario,
                                              BigDecimal total) {


}
