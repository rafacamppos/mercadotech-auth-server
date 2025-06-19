# mercadotech-auth-server

This project demonstrates a simple hexagonal architecture. A use case service is
provided to create and validate JWT Bearer tokens using a client identifier and
client secret. The domain exposes a `TokenService` port, implemented by
`JwtTokenService` in the adapter layer. The application layer exposes
`TokenUseCase` for interacting with the domain. Lombok annotations
are used to reduce boilerplate in service implementations.
