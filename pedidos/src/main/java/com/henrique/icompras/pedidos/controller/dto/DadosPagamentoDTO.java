package com.henrique.icompras.pedidos.controller.dto;

import com.henrique.icompras.pedidos.model.enums.TipoPagamento;

public record DadosPagamentoDTO(
        String dados,
        TipoPagamento tipoPagamento
) {
}
