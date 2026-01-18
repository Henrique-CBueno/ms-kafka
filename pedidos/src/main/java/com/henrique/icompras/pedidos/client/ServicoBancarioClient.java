package com.henrique.icompras.pedidos.client;

import com.henrique.icompras.pedidos.model.Pedido;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@Slf4j
public class ServicoBancarioClient {

    public String solicitarPagamento(Pedido pedido) {
        log.info("Solicitando pagamento para o pedido: {}", pedido.getCodigo());
        String uuid = UUID.randomUUID().toString();
        log.info("Codigo de pagamento do pedido {} é: {}", pedido.getCodigo(), uuid);
        return uuid;
    }

}
