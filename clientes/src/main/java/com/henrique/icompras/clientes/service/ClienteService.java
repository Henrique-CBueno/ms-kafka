package com.henrique.icompras.clientes.service;

import com.henrique.icompras.clientes.model.Cliente;
import com.henrique.icompras.clientes.repository.ClienteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ClienteService {

    private final ClienteRepository clienteRepository;

    public Cliente salvar(Cliente cliente) {
        return clienteRepository.saveAndFlush(cliente);
    }

    public Optional<Cliente> obterClientePorCodigo(Long codigo) {
        return clienteRepository.findById(codigo);
    }

    public void deletar(Cliente cliente) {
        cliente.setAtivo(false);
        clienteRepository.saveAndFlush(cliente);
    }
}
