package com.henrique.icompras.pedidos.subscriber;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.henrique.icompras.pedidos.service.AtualizacaoStatusPedidoService;
import com.henrique.icompras.pedidos.subscriber.representation.AtualizacaoStatusPedidoRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AtualizacaoStatusPedidoSubscriber {

    private final AtualizacaoStatusPedidoService atualizacaoStatusPedidoService;
    private final ObjectMapper objectMapper;

    @KafkaListener(groupId = "${spring.kafka.consumer.group-id}", topics = {
            "${icompras.config.kafka.topics.pedidos-faturados}",
            "${icompras.config.kafka.topics.pedidos-enviados}"
    })
    public void receberAtualizacao(String json) {
        log.info("Recebendo Atualização de status: {}", json);

        try {
            AtualizacaoStatusPedidoRepresentation atualizacaoStatus = objectMapper.readValue(json, AtualizacaoStatusPedidoRepresentation.class);
            atualizacaoStatusPedidoService.atualizarStatus(atualizacaoStatus.codigo(),
                    atualizacaoStatus.status(),
                    atualizacaoStatus.urlNotaFiscal(),
                    atualizacaoStatus.codigoRastreio());

            log.info("PEDIDO ATUALIZADO COM SUCESSO");
        } catch (JsonProcessingException e) {
            log.error("ERRO AO ATUALIZAR O STATUS DO PEDIDO; {}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
