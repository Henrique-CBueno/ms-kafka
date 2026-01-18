package com.henrique.icompras.pedidos.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.henrique.icompras.pedidos.client")
public class ClientsConfig {
}
