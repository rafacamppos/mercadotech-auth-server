# mercadotech-auth-server

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

The pod name is taken from the `POD_NAME` environment variable and the correlation ID comes from the MDC key `correlation_id`.
