---

# API de Gerenciamento de Pagamentos

O objetivo deste projeto é criar uma API para possibilitar o recebimento de pagamentos de débitos de pessoas físicas e jurídicas. A API deve permitir o armazenamento e a atualização do status dos pagamentos, bem como listar e filtrar os pagamentos recebidos.

## Tecnologias

- **Spring Boot**: Framework utilizado para o desenvolvimento da API.
- **Java**: Versão 17
- **H2**: Banco de dados em memória para armazenamento dos pagamentos.
- **Swagger**: Interface interativa para explorar e testar os endpoints da API.
- **Lombok**: Criação automática de código _boilerplate_
- **Mapstruct**: Gerador de código que simplifica a implementação de _mappings_ entre classes

## Funcionalidades

1. **Recebimento de Pagamentos**

   A API é capaz de receber um pagamento com os seguintes campos:
   - **Código do Débito**: Valor inteiro
   - **CPF ou CNPJ do Pagador**: String
   - **Método de Pagamento**: Pode ser `BOLETO`, `PIX`, `CARTAO_CREDITO` ou `CARTAO_DEBITO`
   - **Número do Cartão**: Somente se o método de pagamento for `CARTAO_CREDITO` ou `CARTAO_DEBITO`
   - **Valor do Pagamento**: Valor numérico

2. **Atualização do Status do Pagamento**

   A API é capaz de atualizar o status de um pagamento conforme as seguintes regras:
   - **Pendente de Processamento**: Pode ser alterado para `PROCESSADO_COM_SUCESSO` ou `PROCESSADO_COM_FALHA`.
   - **Processado com Sucesso**: Status não pode ser alterado.
   - **Processado com Falha**: Pode ser alterado para `PENDENTE_PROCESSAMENTO`.

3. **Listagem e Filtragem de Pagamentos**

   A API deve listar todos os pagamentos recebidos e oferecer filtros de busca:
   - **Por Código do Débito**
   - **Por CPF/CNPJ do Pagador**
   - **Por Status do Pagamento**

4. **Exclusão Lógica**

   A API realiza exclusão lógica de um pagamento, mantendo-o no banco de dados com status `inativo`, desde que o pagamento ainda esteja com status `PENDENTE_PROCESSAMENTO` ou `PROCESSADO_COM_FALHA`.


## Como Executar

1. **Clonar o Repositório**

   ```bash
   git clone https://github.com/JuniorLopes09/payment-manager.git
   ```

3. **Executar a Aplicação com Docker**

   ```bash
   docker-compose up
   ```


## Documentação da API com Swagger

Para facilitar o entendimento e a integração com a API, a documentação da API é gerada automaticamente usando o [Swagger](https://swagger.io/).

### Acessando a Documentação Swagger

Após iniciar a aplicação, você pode acessar a interface Swagger em:

```
http://localhost:8080/api/v1/swagger-ui.html
```



## Endpoints da API

- **Receber Pagamento**

  `POST /pagamentos`

  **Payload**:

  ```json
  {
    "codigo": 123,
    "cpfCnpjPagador": "12345678909",
    "metodoPagamento": "CARTAO_CREDITO",
    "numeroCartao": "5554222248513136",
    "valor": 150.00
  }
  ```

- **Atualizar Status do Pagamento**

  `PUT /pagamentos/{id}/status`

  **Payload**:

  ```json
  {
    "status": "PROCESSADO_COM_SUCESSO"
  }
  ```

- **Listar Pagamentos**

  `GET /pagamentos`

  **Parâmetros de Query**:
  - `codigo`
  - `cpfCnpjPagador`
  - `status`

- **Excluir Pagamento (Lógica)**

  `DELETE /pagamentos/{id}`

## Dúvidas

Se você tiver alguma dúvida sobre o projeto, sinta-se à vontade para entrar em contato.


---