package com.henrique.icompras.logistica.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.henrique.icompras.logistica.service.EnvioPedidoService;
import com.henrique.icompras.logistica.subscriber.representation.AtualizacaoFaturamentoRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class FaturamentoSubscriber {

    private final ObjectMapper objectMapper;
    private final EnvioPedidoService envioPedidoService;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = "${icompras.config.kafka.topics.pedidos-faturados}")
    public void listen(String json) {
        log.info("RECEBENDO PEDIDO PARA ENVIO: {}", json);

        try {
            AtualizacaoFaturamentoRepresentation representation =
                    objectMapper.readValue(json, AtualizacaoFaturamentoRepresentation.class);

            envioPedidoService.enviar(representation.codigo(),
                    representation.urlNotaFiscal());

            log.info("Pedido de codigo {} processado com sucesso", representation.codigo());

        } catch (Exception e) {
            log.error("ERRO AO PREPARAR PEDIDO PARA ENVIO, {} ", e.getMessage());
            throw new RuntimeException(e);
        }

    }

}
