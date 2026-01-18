package com.henrique.icompras.logistica.publisher;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.henrique.icompras.logistica.model.AtualizacaoEnvioPedido;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class EnvioPedidoPublisher {

    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${icompras.config.kafka.topics.pedidos-enviados}")
    private String topico;

    public void enviar(AtualizacaoEnvioPedido atualizacaoEnvioPedido) {

        log.info("PUBLICANDO PEDIDO ENVIADO: {}", atualizacaoEnvioPedido.codigo());

        try {
            String json = objectMapper.writeValueAsString(atualizacaoEnvioPedido);
            kafkaTemplate.send(topico, "dados", json);
            log.info("Publicado o pedido {} enviado, codigo de rastreio é: {}", atualizacaoEnvioPedido.codigo(), atualizacaoEnvioPedido.codigoRastreio());

        } catch (Exception e) {
            log.error("erro ao publicar o envio do pedido: {}", e.getMessage());
        }

    }
}
