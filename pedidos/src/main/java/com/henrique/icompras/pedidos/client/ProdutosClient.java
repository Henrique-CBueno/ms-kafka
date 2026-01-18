package com.henrique.icompras.pedidos.client;

import com.henrique.icompras.pedidos.client.representation.ProdutoRepresentation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "produtos", url = "${icompras.pedidos.clients.produtos.url}")
public interface ProdutosClient {

    @GetMapping(value = "{codigo}")
    ResponseEntity<ProdutoRepresentation> obterDados(@PathVariable Long codigo);

}
