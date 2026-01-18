package com.henrique.icompras.pedidos.subscriber.representation;

import com.henrique.icompras.pedidos.model.enums.StatusPedido;

public record AtualizacaoStatusPedidoRepresentation(Long codigo,
                                                    StatusPedido status,
                                                    String urlNotaFiscal,
                                                    String codigoRastreio) {
}
