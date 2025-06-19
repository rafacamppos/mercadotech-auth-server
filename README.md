# mercadotech-auth-server

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

## Métricas

O projeto expõe métricas via Spring Boot Actuator em `/actuator/prometheus`, permitindo integração com o Prometheus.


