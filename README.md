# 🧩 Sistema de Pedidos com Microsserviços (Spring Boot + Kafka)

Este projeto implementa um **sistema completo de pedidos** utilizando **arquitetura de microsserviços**, comunicação **assíncrona com Apache Kafka** e **Spring Boot**. Ele cobre todo o ciclo de um pedido, desde a criação até o faturamento, envio e rastreamento.

A arquitetura foi pensada para ser **escalável, desacoplada e orientada a eventos**, seguindo boas práticas de sistemas distribuídos.

---

## 🏗️ Arquitetura Geral

O sistema é composto por múltiplos microsserviços independentes, cada um com sua própria base de dados, comunicando-se principalmente por **eventos Kafka** e, em alguns casos, por **webhooks**.

Fluxo resumido:

1. O usuário cria um pedido (Web ou Mobile)
2. O serviço de Pedidos registra o pedido
3. O pagamento é processado e seu status é atualizado
4. O pedido pago é publicado em um tópico Kafka
5. O serviço de Faturamento consome o evento e gera a fatura (PDF)
6. A fatura é publicada
7. O serviço de Logística consome o evento e gera o envio/rastreamento

---

## 🔁 Comunicação

- **Apache Kafka** para comunicação assíncrona
- **Publisher / Consumer** com consumo paralelo
- **Eventos orientados a domínio** (PedidoPago, PedidoFaturado, PedidoEnviado)
- **Webhooks** para integração entre microsserviços quando necessário

---

## 🧠 Microsserviços

### 📦 Produtos Service

- Gerenciamento de produtos
- Persistência em PostgreSQL
- Comunicação apenas interna

### 👤 Clientes Service

- Cadastro e manutenção de clientes
- Base de dados isolada

### 🧾 Pedidos Service

- Criação e atualização de pedidos
- Orquestra o fluxo inicial
- Publica eventos de pagamento

### 💰 Faturamento Service

- Consome eventos de pedidos pagos
- Gera faturas em PDF
- Utiliza **Jasper Reports**
- Armazena arquivos no **MinIO (Buckets)**

### 🚚 Logística Service

- Consome eventos de pedidos faturados
- Gera dados de envio e rastreamento
- Publica eventos de pedidos enviados

---

## 🧰 Tecnologias Utilizadas

### Backend

- **Java 11**
- **Spring Boot**
- **Spring Data JPA**
- **MapStruct** (DTO ↔ Entity)

### Mensageria

- **Apache Kafka**
- **Kafka Producer & Consumer**
- **Consumo paralelo**

### Banco de Dados

- **PostgreSQL** (um por microsserviço)

### Armazenamento de Arquivos

- **MinIO**
- Buckets para PDFs de faturamento

### Relatórios

- **Jasper Reports**
- Geração de PDFs personalizados

---

## 📄 Geração de PDFs

O serviço de Faturamento utiliza **Jasper Reports** para gerar documentos PDF com layout customizado, contendo:

- Dados do cliente
- Produtos do pedido
- Valores
- Identificação e status

Os arquivos são armazenados em buckets no MinIO.

---

## 🔐 Segurança

- Comunicação protegida entre serviços
- Separação total de bancos de dados
- Serviços acessados apenas via APIs controladas

---

## ▶️ Como Executar o Projeto

### Pré-requisitos

- Java 11+
- Docker & Docker Compose
- Apache Kafka
- PostgreSQL
- MinIO

### Subindo a infraestrutura

```bash
docker-compose up -d
```

### Executando os serviços

```bash
./mvnw spring-boot:run
```

(Executar cada microsserviço individualmente)

---

## 📌 Observações Importantes

- Cada microsserviço possui **banco de dados próprio**
- Comunicação síncrona foi evitada ao máximo
- Projeto focado em **boas práticas de microsserviços**
- Ideal para estudo e demonstração de arquitetura distribuída

---

## 📷 Arquitetura do Sistema

A imagem abaixo representa o fluxo completo de eventos e comunicação entre os microsserviços:

> _(Adicionar aqui a imagem da arquitetura presente neste repositório)_

---

## 🚀 Objetivo do Projeto

Este projeto tem como objetivo:

- Demonstrar arquitetura de microsserviços na prática
- Aplicar Kafka em cenários reais
- Trabalhar com eventos, escalabilidade e desacoplamento
- Servir como portfólio técnico

---

## 👨‍💻 Autor

**Henrique Bueno**

---

Se você achou este projeto interessante, deixe uma ⭐ no repositório!
