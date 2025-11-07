# API de Pagamentos

API REST desenvolvida em **Spring Boot** para recebimento de pagamentos de débitos de pessoas físicas e jurídicas.

---

## Tecnologias
- Java 17
- Spring Boot
- H2 Database
- Maven
- Swagger (documentação)
- JUnit + Mockito (testes unitários)

---

## Endpoints:

| Método | Endpoint | Descrição |
|--------|-----------|------------|
| POST | /api/pagamentos | Cria novo pagamento |
| PATCH | /api/pagamentos/{id} | Atualiza status de pagamento |
| GET | /api/pagamentos | Lista pagamentos (com filtros) |
| DELETE | /api/pagamentos/{id} | Exclusão lógica (somente pendentes) |

---

## Testes Unitários
Testes unitários implementados com **JUnit 5** e **Mockito** cobrindo:
- Criação de pagamento
- Atualização de status
- Exclusão lógica
- Validações de CPF/CNPJ e número do cartão

---

## Como testar rodando local:

1. Via Swagger, além de ter acessoa a documentação

   ```http://localhost:8080/pagamentos-api/swagger-ui.html#```

2. Utilizando Insomnia, Postman...
   
   2.1 - Novo Pagamento

   POST

   ```http://localhost:8080/pagamentos-api/api/pagamentos```
   
   Json exemplo:
   ```Json
   {
    "codigoDebito": 101,
    "cpfCnpj": "12345678901",
    "metodoPagamento": "PIX",
    "valor": 250.75
    }

  
  2.2 - Atualizar status 
  
  PATCH 
  
  ```http://localhost:8080/pagamentos-api/api/pagamentos/{id}```

  Json exemplo:
  ```Json
  {
    "novoStatus": "PROCESSADO_SUCESSO"
  }
```

  
  2.3 - Exclusão lógica de pagamento
  
  DELETE
  
  ```http://localhost:8080/pagamentos-api/api/pagamentos/{id}```
  
  Observações:
  
  Não remove o registro do banco, apenas marca como INATIVO.
  
  Somente permitido se o status atual for PENDENTE_PROCESSAMENTO.

  
  2.4 - Listar pagamentos com filtros

  
  GET 
  
  ```http://localhost:8080/pagamentos-api/api/pagamentos```

  Parametros:
  - cpfCnpj
  - codigoDebito
  - statusPagamento


---

## Acesso ao banco de dados H2:

- URL de acesso: ```http://localhost:8080/pagamentos-api/h2-console/```
- JDBC URL: ```jdbc:h2:mem:pagamentos-db```
- User name: ```sa```
- Password: ```sa```
