package com.henrique.icompras.logistica.service;

import com.henrique.icompras.logistica.model.AtualizacaoEnvioPedido;
import com.henrique.icompras.logistica.model.StatusPedido;
import com.henrique.icompras.logistica.publisher.EnvioPedidoPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
@RequiredArgsConstructor
public class EnvioPedidoService {

    private final EnvioPedidoPublisher envioPedidoPublisher;

    public void enviar(Long codigoPedido, String urlNotaFiscal) {
        String codigoRasteiro = gerarCodigoRastreio(codigoPedido);

        AtualizacaoEnvioPedido atualizacaoRepresentation =
                new AtualizacaoEnvioPedido(codigoPedido, StatusPedido.ENVIADO, codigoRasteiro);

        envioPedidoPublisher.enviar(atualizacaoRepresentation);

    }

    private String gerarCodigoRastreio(Long codigoPedido) {
        //AB123456789BR-{codigoPedido}

        Random random = new Random();

        char letra1 = (char) ('A' + random.nextInt(26));
        char letra2 = (char) ('A' + random.nextInt(26));

        int numeros = 100000000 + random.nextInt(900000000);

        return "" + letra1 + letra2 + numeros + "BR-" + codigoPedido;

    }
}
