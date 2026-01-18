package com.henrique.icompras.faturamento.subscriber;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.henrique.icompras.faturamento.generator.GeradorNotaFiscalService;
import com.henrique.icompras.faturamento.mapper.PedidoMapper;
import com.henrique.icompras.faturamento.model.Pedido;
import com.henrique.icompras.faturamento.subscriber.representation.DetalhePedidoRepresentation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PedidoPagoSubscriber {

    private final ObjectMapper mapper;
    private final GeradorNotaFiscalService geradorNotaFiscalService;
    private final PedidoMapper pedidoMapper;

    @KafkaListener(groupId = "icompras.faturamento", topics = "${icompras.config.kafka.topics.pedidos-pagos}")
    public void listen(String json) {

        try {
            log.info("RECEBENDO PEDIDO PARA FATURAMENTO: {}", json);
            DetalhePedidoRepresentation representation = mapper.readValue(json, DetalhePedidoRepresentation.class);
            Pedido pedido = pedidoMapper.map(representation);
            geradorNotaFiscalService.gerar(pedido);
        } catch (Exception e) {
            log.error("ERRO NA CONSUMAÇAO NO TOPICO DE PEDIDOS PAGOS {}", e.getMessage());
            throw new RuntimeException("ERRO NA CONSUMAÇAO NO TOPICO DE PEDIDOS PAGOS");
        }

    }
}
