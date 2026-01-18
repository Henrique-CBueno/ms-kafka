package com.henrique.icompras.pedidos.publisher;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.henrique.icompras.pedidos.model.Pedido;
import com.henrique.icompras.pedidos.publisher.representation.DetalhePedidoRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PagamentoPublisher {

    private final DetalhePedidoMapper mapper;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${icompras.config.kafka.topics.pedidos-pagos}")
    private String topico;


    public void publicar(Pedido pedido) {
        log.info("Publicando pedido de codigo: {}, Pago!", pedido.getCodigo());

        try {
            DetalhePedidoRepresentation representation = mapper.map(pedido);
            String json = objectMapper.writeValueAsString(representation);
            kafkaTemplate.send(topico, "dados", json);

            log.info("Pedido de codigo: {}, PUBLICADO!", pedido.getCodigo());

        } catch (JsonProcessingException e) {
            log.error("ERRO AO PROCESSAR O JSON: ", e);
        } catch (RuntimeException e) {
            log.error("ERRO AO PUBLICAR NO TÓPICO DE PEDIDOS: ", e);
        }
    }
}
