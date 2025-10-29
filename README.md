# 🍽️ EZ-REST API: Sistema de Gestão de Pedidos via QR Code

[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.3.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21-red)](https://www.oracle.com/java/technologies/javase/jdk21-archive-downloads.html)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Swagger UI](https://img.shields.io/badge/API_Docs-Swagger_UI-85EA2D)](http://localhost:8080/swagger-ui.html)

API RESTful para gerenciar o ciclo de vida de pedidos em um restaurante, desde a criação do cardápio e das mesas até o fechamento da conta, utilizando um modelo de Comanda/Extrato acessível via QR Code.

## 🌟 Requisitos Funcionais (RFs) Implementados

* **RF001 (CRUD Cardápio):** Criação, Leitura, Atualização e Deleção de Produtos.
* **RF002 (CRUD Mesas):** Criação, Leitura, Atualização e Deleção de Mesas.
* **RF003 (Pedidos):** Abrir Comanda, adicionar `OrderItem`s com cálculo automático de valor total e fechamento.
* **RF004 (Extrato QR Code):** Endpoint público para consultar o consumo de uma mesa ativa (simulando a leitura do QR Code).

## 🚀 Tecnologias Utilizadas

| Componente | Tecnologia | Descrição |
| :--- | :--- | :--- |
| **Framework** | Spring Boot 3 | Simplifica a configuração e o desenvolvimento de aplicações Java. |
| **Persistência** | Spring Data JPA / Hibernate | Mapeamento Objeto-Relacional. |
| **Banco de Dados** | H2 Database (Padrão) | Banco de dados em memória para desenvolvimento e testes. |
| **Documentação** | SpringDoc OpenAPI (Swagger) | Geração automática de documentação da API e UI interativa. |
| **Utilitários** | Lombok | Reduz código boilerplate (getters, setters, construtores). |
| **Validação** | Spring Validation | Validação de dados de entrada com anotações (`@Valid`, `@NotEmpty`, etc.). |

## ⚙️ Configuração do Projeto

### Pré-requisitos

* JDK 21 ou superior
* Maven ou Gradle
* Cliente REST (Postman ou Swagger UI)

### Execução Local

1.  **Clone o repositório:**
    ```bash
    git clone https://github.com/torresgdev/RestaurantManager
    ```

2.  **Inicie a aplicação:**
    ```bash
    # Via Maven
    ./mvnw spring-boot:run 
    ```
    A aplicação será iniciada na porta padrão `8080`.

## 📖 Endpoints da API (Swagger UI)

Após iniciar a aplicação, acesse a documentação interativa para testar todos os endpoints:

👉 **[http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)**

---

## 💡 Guia de Uso Rápido (Fluxo Principal)

Para testar o ciclo completo de um pedido, siga esta sequência:

### 1. Preparar o Ambiente

| Ação | Método | Endpoint | Notas |
| :--- | :--- | :--- | :--- |
| **Criar Produto** | `POST` | `/products` | Necessário para ter itens no pedido. (Guarde o `id`). |
| **Criar Mesa** | `POST` | `/tables` | Necessário para ter uma mesa para alugar. (Guarde o `tableId`). |

### 2. Ciclo de Vida do Pedido

| Ação | Método | Endpoint | Descrição |
| :--- | :--- | :--- | :--- |
| **Abrir Comanda**| `POST` | `/orders/open/{tableId}` | Inicia o consumo. Status da Mesa muda para `OCCUPIED`. |
| **Adicionar Itens**| `POST` | `/orders/add-items` | Envia o JSON com a lista de `productId`s e `quantity`. |
| **Consultar Extrato**| `GET` | `/extrato/{tableId}` | **Endpoint Público.** Verifica o valor total e os itens (Simulação do QR Code). |
| **Fechar Comanda**| `POST` | `/orders/close/{tableId}` | Finaliza o consumo. Status da Comanda muda para `CLOSED` e a Mesa volta para `FREE`. |

## 🛑 Tratamento de Erros

A API utiliza um `GlobalExceptionHandler` para retornar códigos de status HTTP claros em caso de falha de regra de negócio:

| Código HTTP | Exceção Lançada | Descrição |
| :--- | :--- | :--- |
| **`404 Not Found`** | `TableNotFoundException`, `ProductNotFoundException` | Recurso solicitado não existe. |
| **`409 Conflict`** | `ConflictNameException`, `BusinessLogicException` | Tentativa de abrir comanda em mesa já ocupada, ou outra regra de negócio violada. |
|
| **`500 Internal Server Error`** | Exceção não tratada | Erro interno do servidor. |

---
