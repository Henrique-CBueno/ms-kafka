package com.henrique.icompras.pedidos.controller;

import com.henrique.icompras.pedidos.controller.dto.RecebimentoCallbackPagamentoDTO;
import com.henrique.icompras.pedidos.exceptions.PedidoNaoEncontradoException;
import com.henrique.icompras.pedidos.service.PedidoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pedidos/callback-pagamentos")
@RequiredArgsConstructor
public class RecebimentoCallbackPagamentoController {

    private final PedidoService pedidoService;

    @PostMapping
    public ResponseEntity<Object> atualizarStatusPagamento(
                @RequestBody RecebimentoCallbackPagamentoDTO body,
                @RequestHeader(required = true, name = "apiKey") String apiKey
            ) {
        try {

            pedidoService.atualizarStatusPagamento(
                    body.codigo(),
                    body.chavePagamento(),
                    body.status(),
                    body.observacoes()
            );

        } catch (PedidoNaoEncontradoException e) {

            return ResponseEntity.notFound().build();

        }

        return ResponseEntity.ok().build();

    }
}
