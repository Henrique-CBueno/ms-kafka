package com.henrique.icompras.pedidos.controller.dto;

import com.henrique.icompras.pedidos.model.enums.TipoPagamento;

public record AdicionarNovoPagamentoDTO(String dados,
                                        TipoPagamento tipoPagamento) {
}
