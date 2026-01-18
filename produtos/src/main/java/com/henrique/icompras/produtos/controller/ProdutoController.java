package com.henrique.icompras.produtos.controller;

import com.henrique.icompras.produtos.model.Produto;
import com.henrique.icompras.produtos.service.ProdutoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping("produtos")
@RequiredArgsConstructor
public class ProdutoController {

    private final ProdutoService produtoService;

    @PostMapping
    public ResponseEntity<Produto> salvar(@RequestBody Produto produto) {
        Produto produtoRes = produtoService.salvar(produto);
        return ResponseEntity.ok(produtoRes);
    }

    @GetMapping(value = "{codigo}")
    public ResponseEntity<Produto> obterDados(@PathVariable Long codigo) {
        return produtoService
                .obterProdutoPorCodigo(codigo)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("{codigo}")
    public ResponseEntity<Void> deletar(@PathVariable Long codigo) {
        Produto produto = produtoService
                .obterProdutoPorCodigo(codigo)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Produto inexistente"
                ));

        produtoService.deletar(produto);

        return ResponseEntity.noContent().build();
    }

}
