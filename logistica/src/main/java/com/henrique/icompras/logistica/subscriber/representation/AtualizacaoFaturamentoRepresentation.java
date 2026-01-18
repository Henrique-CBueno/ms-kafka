package com.henrique.icompras.logistica.subscriber.representation;

import com.henrique.icompras.logistica.model.StatusPedido;

public record AtualizacaoFaturamentoRepresentation(Long codigo,
                                                   StatusPedido status,
                                                   String urlNotaFiscal) {
}
