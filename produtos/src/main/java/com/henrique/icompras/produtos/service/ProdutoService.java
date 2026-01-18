package com.henrique.icompras.produtos.service;

import com.henrique.icompras.produtos.model.Produto;
import com.henrique.icompras.produtos.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public Produto salvar(Produto produto) {
        return produtoRepository.saveAndFlush(produto);
    }

    public Optional<Produto> obterProdutoPorCodigo(Long codigo) {
        return produtoRepository.findById(codigo);
    }

    public void deletar(Produto produto) {
        produto.setAtivo(false);
        produtoRepository.saveAndFlush(produto);
    }
}
