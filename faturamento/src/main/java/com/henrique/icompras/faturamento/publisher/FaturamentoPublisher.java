package com.henrique.icompras.faturamento.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.henrique.icompras.faturamento.model.Pedido;
import com.henrique.icompras.faturamento.publisher.representation.AtualizacaoStatusPedido;
import com.henrique.icompras.faturamento.publisher.representation.StatusPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class FaturamentoPublisher {

    @Value("${icompras.config.kafka.topics.pedidos-faturados}")
    private String topic;

    private final KafkaTemplate<String, String> kafkaTemplate;
    private final ObjectMapper objectMapper;

    public void publicar(Pedido pedido, String urlNotaFiscal) {

        try {
            AtualizacaoStatusPedido representation = new AtualizacaoStatusPedido(pedido.codigo(), StatusPedido.FATURADO, urlNotaFiscal);

            String json = objectMapper.writeValueAsString(representation);

            kafkaTemplate.send(topic, "dados", json);
        } catch (JsonProcessingException e) {
            log.error("ERRO AO COLOCAR O PEDIDO {} NO TOPICO {}, ", pedido, topic + e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
