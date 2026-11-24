# Baozi Store

API REST de uma loja de pãezinhos chineses. Cada pedido associa um cliente a um produto e registra a quantidade comprada.

## Execução

1. Instale um **JDK 17 ou superior** e habilite o comando `java` no terminal.
2. Abra o terminal na pasta que contém o arquivo `pom.xml`.
3. Inicie a aplicação com o Maven Wrapper. A primeira execução baixa o Maven e as dependências.

   **Windows PowerShell**

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

   **Linux ou macOS**

   ```sh
   sh ./mvnw spring-boot:run
   ```

A API fica disponível em `http://127.0.0.1:8080`. O H2 cria e mantém os dados em `dados/baozi-store.mv.db` entre execuções. Para encerrar a API, pressione **Ctrl+C** no terminal.

## Endpoints

| Entidade | Cadastrar | Listar | Consultar por ID | Excluir |
| --- | --- | --- | --- | --- |
| Cliente | `POST /clientes` | `GET /clientes` | `GET /clientes/{id}` | `DELETE /clientes/{id}` |
| Produto | `POST /produtos` | `GET /produtos` | `GET /produtos/{id}` | `DELETE /produtos/{id}` |
| Pedido | `POST /pedidos` | `GET /pedidos` | `GET /pedidos/{id}` | `DELETE /pedidos/{id}` |

### Exemplos de cadastro

Envie os POSTs com `Content-Type: application/json`. Use no pedido os IDs retornados nos cadastros do cliente e do produto.

**Cliente**

```json
{"nome":"Cliente Exemplo","clienteDesde":"2026-11-24"}
```

**Produto**

```json
{"nome":"Baozi de Legumes","preco":8.50,"estoque":true}
```

**Pedido**

```json
{"clienteId":1,"produtoId":1,"quantidade":3}
```

O POST retorna `201 Created` com o registro em JSON e o cabeçalho `Location`. As consultas bem-sucedidas retornam `200 OK`. A exclusão retorna `204 No Content`. Campos inválidos resultam em `400 Bad Request`. Um ID inexistente resulta em `404 Not Found`. Clientes e produtos vinculados a pedidos só podem ser excluídos depois dos respectivos pedidos. A API retorna `409 Conflict` se esse vínculo impedir a exclusão.

## Estrutura

- `model` contém as entidades JPA e os relacionamentos.
- `repository` contém as interfaces do Spring Data JPA.
- `controller` recebe as requisições, valida os dados e produz as respostas HTTP.
