package com.henrique.icompras.pedidos.service;

import com.henrique.icompras.pedidos.model.Pedido;
import com.henrique.icompras.pedidos.model.enums.StatusPedido;
import com.henrique.icompras.pedidos.repository.PedidoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AtualizacaoStatusPedidoService {

    private final PedidoRepository pedidoRepository;

    @Transactional
    public void atualizarStatus(Long codigo,
                                StatusPedido status,
                                String urlNotaFiscal,
                                String rastreio) {

        pedidoRepository.findById(codigo).ifPresent(
                pedido -> {
                    atualizarStatusPedido(status, urlNotaFiscal, rastreio, pedido);
                }
        );

    }

    private static void atualizarStatusPedido(StatusPedido status, String urlNotaFiscal, String rastreio, Pedido pedido) {
        pedido.setStatus(status);
        if (urlNotaFiscal != null) {
            pedido.setUrlNotaFiscal(urlNotaFiscal);
        }

        if (rastreio != null) {
            pedido.setCodigoRastreio(rastreio);
        }
    }
}
