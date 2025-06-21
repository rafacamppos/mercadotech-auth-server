
This project demonstrates a simple hexagonal architecture. A use case service is
provided to create and validate JWT Bearer tokens using a client identifier and
client secret. The domain exposes a `TokenService` port, implemented by
`JwtTokenService` in the adapter layer. The application layer exposes
`TokenUseCase` for interacting with the domain. Its implementation,
`TokenUseCaseImpl`, now lives in the `application.service` package. Lombok annotations
are used to reduce boilerplate in service implementations.

## Logging

The application logs messages in JSON format to make aggregation easier. Each entry contains the timestamp, log level, class name, Kubernetes pod name and a correlation ID if available:

```json
{
  "timestamp": "2025-06-19T14:00:00Z",
  "level": "INFO",
  "class": "class-name",
  "pod_name": "order-service-abc123",
  "message": "Order processed successfully",
  "correlation_id": "uuid"
}
```

The pod name is taken from the `POD_NAME` environment variable. The correlation
ID is kept in a thread-local context under the key `correlation_id` and added to
the MDC when a message is logged. Classes that generate log entries should
instantiate a `DefaultStructuredLogger` and delegate logging to its `info`,
`warn` and `error` methods. These methods accept the log message and an optional
correlation ID. If none is provided, the logger reuses the one stored in the
context or generates a random UUID, storing it for subsequent entries.
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
docker run -d --name auth-server \
  --network auth-network \
  -p 8080:8080 auth-server
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

### Redis com Docker

Para armazenar o token do client secret em cache, execute:

```bash
./scripts/start-redis.sh
```

O script irá criar um container `auth-redis` ouvindo na porta `6379`.

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

### Monitoramento com Prometheus e Grafana

Com a aplicação em execução em um container `auth-server`, é possível iniciar
Prometheus e Grafana executando:

```bash
./scripts/start-monitoring.sh
```

O script cria uma rede Docker `auth-network`, constrói uma imagem personalizada
do Prometheus com uma configuração que coleta métricas do `auth-server` e
inicia também o Grafana. Após a execução, acesse `http://localhost:3000` com as
credenciais padrão `admin`/`admin` e adicione o Prometheus disponível em
`http://auth-prometheus:9090` como fonte de dados para visualizar os gráficos.


## Integração Contínua

Este repositório utiliza o GitHub Actions para compilar e executar os testes do projeto a cada push ou pull request. O fluxo de trabalho é definido em `.github/workflows/maven.yml` e executa o comando:

```bash
mvn -B verify
```

Isso garante que o build está sempre saudável antes de novas alterações serem mescladas.

