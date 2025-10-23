# 🍔 DOCUMENTAÇÃO DA API DE GESTÃO DE PEDIDOS DE RESTAURANTE

## 1. Visão Geral e Conceito de Negócio (Passo 1)

### 1.1. Introdução

#### 1.1.1. Objetivo Principal
A API de Gestão de Pedidos tem como objetivo centralizar e digitalizar o fluxo de pedidos de um restaurante. Ela deve gerenciar a abertura e fechamento de comandas por mesa, registrar pedidos feitos por garçons e, crucialmente, permitir que clientes acompanhem seus gastos em tempo real via leitura de um QR Code.

#### 1.1.2. Escopo do Projeto
| Funcionalidade (INCLUÍDA) | Funcionalidade (NÃO INCLUÍDA - V2) |
| :--- | :--- |
| CRUD (Criação, Leitura, Atualização, Deleção) de Pedidos e Itens. |
| Cálculo automático do valor total da comanda por mesa. | Gestão de Estoque e Alerta de Produtos em Falta. |
| Endpoints públicos para consulta do extrato da mesa via ID (QR Code). | Módulo de Login/Permissões avançadas para gerentes. |
| Autenticação simples baseada em token para Garçons. | Relatórios de vendas e desempenho de garçons. |

### 1.2. Glossário de Termos Chave

| Termo | Definição |
| :--- | :--- |
| **Mesa (ID)** | Identificador único do ponto de consumo (a mesa física). Atua como a **Comanda Ativa** do cliente. |
| **Comanda** | O registro de consumo da Mesa. Guarda todos os pedidos realizados, data de abertura e valor total acumulado. |
| **Produto** | Item disponível no cardápio (ex: Cerveja, Hambúrguer X). Possui nome e preço unitário. |
| **Pedido** | Uma requisição feita por um Garçom para adicionar um ou mais Produtos a uma Comanda. |
| **Extrato** | A visualização pública e em tempo real do consumo da Mesa (liberada via QR Code). |

### 1.3. Requisitos

| ID | Tipo | Requisito |
| :--- | :--- | :--- |
| **RF001** | Funcional | O sistema deve permitir a abertura de uma nova Comanda (associada a um ID de Mesa). |
| **RF002** | Funcional | O sistema deve permitir que um Garçom adicione múltiplos Itens a uma Comanda Ativa. |
| **RF003** | Funcional | O sistema deve calcular e manter atualizado o **Valor Total** de cada Comanda. |
| **RF004** | Funcional | O cliente deve conseguir visualizar o extrato (pedidos, quantidade, valores) apenas com o ID da Mesa. |
| **RNF001** | Não Funcional | A API deve ser desenvolvida em **Java** utilizando **Spring Boot**. |
| **RNF002** | Não Funcional | O tempo de resposta para a consulta do Extrato (QR Code) deve ser **inferior a 300ms**. |
| **RNF003** | Não Funcional | A aplicação deve utilizar **Git** para controle de versão. |

---

## 2. Arquitetura e Modelagem de Dados (Passo 2)

### 2.1. Arquitetura

O sistema seguirá um padrão de arquitetura em camadas, utilizando o Spring Boot.

* **Linguagem de Programação:** Java (versão 17+).
* **Framework:** Spring Boot.
* **Banco de Dados:** PostgreSQL.
* **Documentação da API:** OpenAPI / Swagger.

### 2.2. Modelo de Dados (Entidades Chave)

A seguir, a estrutura básica das entidades do sistema:

#### **Entidade: `Product` (Cardápio)**
| Campo   | Tipo | Descrição |
|:--------| :--- | :--- |
| `id`    | UUID/Long | **PK**. Identificador do produto. |
| `name`  | String | Nome do item (ex: "Água sem gás"). |
| `price` | BigDecimal | Preço unitário. |

#### **Entidade: `Mesa`**
| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | Long | **PK**. Número físico da mesa. |
| `status` | Enum | `OCUPADA`, `LIVRE`. |
| `qrCodeUrl` | String | URL para o endpoint de Extrato (ex: `/extrato/{mesaId}`). |

#### **Entidade: `Comanda` (Comanda Ativa da Mesa)**
| Campo | Tipo | Descrição |
| :--- | :--- | :--- |
| `id` | UUID/Long | **PK**. Identificador da Comanda. |
| `mesaId` | Long | **FK** para `Mesa`. |
| `dataHoraAbertura` | LocalDateTime | Registro de quando a Comanda foi aberta. |
| `valorTotal` | BigDecimal | Total acumulado da Comanda. **Atualizado a cada novo Pedido.** |
| `status` | Enum | `ABERTA`, `FECHADA`. |

#### **Entidade: `OrderItem` (Registro de um Produto em uma Comanda)**
| Campo           | Tipo | Descrição                                                              |
|:----------------| :--- |:-----------------------------------------------------------------------|
| `id`            | UUID/Long | **PK**.                                                                |
| `comandaId`     | UUID/Long | **FK** para `Comanda`.                                                 |
| `productId`     | UUID/Long | **FK** para `Product`.                                                 |
| `quantidade`    | Integer | Quantidade pedida.                                                     |
| `precoUnitario` | BigDecimal | Preço do produto no momento do pedido (para evitar variação de preço). |
| `subtotal`      | BigDecimal | Quantidade * PreçoUnitario.                                            |

---

## 3. Endpoints da API (Passo 3)

**Base URL:** `/api/v1`

### 3.1. Autenticação

Todos os endpoints, exceto o de Extrato (`/extrato`), requerem autenticação (ex: JWT / Token) para acesso do Garçom/Sistema.

### 3.2. Gerenciamento de Mesas e Comandas

| Método | Endpoint | Descrição                                   |
| :--- | :--- |:--------------------------------------------|
| `POST` | `/mesas/{mesaId}/abrir` | Abre uma nova Comanda para a Mesa.|
| `GET` | `/mesas/{mesaId}` | Retorna o status e a Comanda Ativa da Mesa. |
| `POST` | `/mesas/{mesaId}/fechar` | Fecha a Comanda, finalizando a conta.       |

### 3.3. Gerenciamento de Pedidos (Garçom)

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/pedidos` | Adiciona um novo Pedido à Comanda Ativa da Mesa. |
| `PATCH` | `/pedidos/{itemId}` | Altera a quantidade de um ItemPedido específico. |
| `DELETE` | `/pedidos/{itemId}` | Remove um ItemPedido da Comanda. |

#### Exemplo de Requisição (POST /pedidos)
```json
{
  "mesaId": 5,
  "itens": [
    {
      "produtoId": "a1b2c3d4-...",
      "quantidade": 2
    },
    {
      "produtoId": "e5f6g7h8-...",
      "quantidade": 1
    }
  ]
}