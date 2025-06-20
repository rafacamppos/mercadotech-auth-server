
This project demonstrates a simple hexagonal architecture. A use case service is
provided to create and validate JWT Bearer tokens using a client identifier and
client secret. The domain exposes a `TokenService` port, implemented by
`JwtTokenService` in the adapter layer. The application layer exposes
`TokenUseCase` for interacting with the domain. Its implementation,
`TokenUseCaseImpl`, now lives in the `application.service` package. Lombok annotations
are used to reduce boilerplate in service implementations.

## Logging

A aplicação registra mensagens em formato JSON para facilitar a agregação. Cada entrada inclui data e hora, nível do log, nome da classe, nome do pod do Kubernetes e um ID de correlação, se disponível:

```json
{
  "timestamp": "2025-06-19T14:00:00Z",
  "level": "INFO",
  "class": "nome-da-classe",
  "pod_name": "order-service-abc123",
  "message": "Pedido processado com sucesso",
  "correlation_id": "uuid"
}
```

O nome do pod é obtido a partir da variável de ambiente `POD_NAME`. O ID de correlação é mantido em um contexto de thread sob a chave `correlation_id` e adicionado ao MDC quando uma mensagem é registrada. As classes que geram logs devem instanciar um `DefaultStructuredLogger` e delegar a ele as chamadas `info`, `warn` e `error`. Esses métodos recebem a mensagem e um ID de correlação opcional. Caso nenhum seja fornecido, o logger reutiliza o ID armazenado no contexto ou gera um UUID aleatório para as próximas entradas.
Este projeto demonstra uma arquitetura hexagonal simples para um servidor de autenticação baseado em Spring Boot. Os serviços disponibilizados permitem gerar e validar tokens JWT utilizando um `clientId` e um `clientSecret`.

## Requisitos

- Java 17
- Maven 3.x

## Como compilar e executar

Para gerar o artefato do projeto execute:

```bash
mvn clean package
```

Para rodar os testes utilize:

```bash
mvn test
```

Após a compilação é possível iniciar a aplicação executando o jar gerado:

```bash
java -jar target/auth-server-0.0.1-SNAPSHOT.jar
```

A API ficará disponível em `http://localhost:8080`.

### Docker

Opcionalmente você pode utilizar o Dockerfile incluso:

```bash
docker build -t auth-server .
docker run -p 8080:8080 auth-server
```

### Banco de dados PostgreSQL com Docker

Para iniciar rapidamente um banco PostgreSQL local execute o script abaixo:

```bash
./scripts/start-postgres.sh
```

O script irá criar um container nomeado `auth-postgres` escutando na porta `5432` com as credenciais definidas em `src/main/resources/application.yaml`.
Com o container em execução, crie a tabela de credenciais executando:

```bash
psql -h localhost -U postgres -d authdb -f scripts/create_credentials_table.sql
```

## Endpoints principais

### `POST /login`

Gera um token JWT. Corpo da requisição:

```json
{
  "clientId": "seu-cliente",
  "clientSecret": "segredo"
}
```

Resposta esperada (HTTP 200):

```json
{
  "token": "<jwt>"
}
```

### `POST /token/validate`

Valida um token previamente obtido. Corpo da requisição:

```json
{
  "token": "<jwt>",
  "clientSecret": "segredo"
}
```

Resposta (HTTP 200):

```json
{
  "valid": true
}
```

Os detalhes completos de cada endpoint podem ser consultados no arquivo [`openapi.yaml`](openapi.yaml).

## Como utilizar o `openapi.yaml`

O arquivo `openapi.yaml` segue a especificação OpenAPI e pode ser importado em
ferramentas como Swagger UI, Postman ou Insomnia para explorar a API
interativamente. Uma maneira rápida de visualizar a documentação é utilizando a
imagem oficial do Swagger UI:

```bash
docker run --rm -p 8081:8080 \
  -e SWAGGER_JSON=/data/openapi.yaml \
  -v $(pwd)/openapi.yaml:/data/openapi.yaml \
  swaggerapi/swagger-ui
```

Após executar o comando acima, acesse `http://localhost:8081` em seu navegador
para consultar e testar os endpoints disponíveis.

## Métricas

O projeto expõe métricas via Spring Boot Actuator em `/actuator/prometheus` e `/actuator/metrics`, permitindo integração com o Prometheus.
Os endpoints de login e validação possuem `Timer`s registrados no `MeterRegistry`,
publicando histogramas e percentis (50%, 95% e 99%) de latência.


## Integração Contínua

Este repositório utiliza o GitHub Actions para compilar e executar os testes do projeto a cada push ou pull request. O fluxo de trabalho é definido em `.github/workflows/maven.yml` e executa o comando:

```bash
mvn -B verify
```

Isso garante que o build está sempre saudável antes de novas alterações serem mescladas.

