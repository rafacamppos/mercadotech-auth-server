package com.mercadotech.authserver.adapter;

import com.mercadotech.authserver.adapter.dto.LoginRequest;
import com.mercadotech.authserver.adapter.dto.TokenResponse;
import com.mercadotech.authserver.adapter.dto.ValidateRequest;
import com.mercadotech.authserver.adapter.dto.ValidateResponse;
import com.mercadotech.authserver.application.useCase.TokenUseCase;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@RequiredArgsConstructor
public class AuthController {

    private final TokenUseCase tokenUseCase;

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@RequestBody LoginRequest request) {
        String token = tokenUseCase.generateToken(request.getClientId(), request.getClientSecret());
        return ResponseEntity.ok(new TokenResponse(token));
    }

    @PostMapping("/token/validate")
    public ResponseEntity<ValidateResponse> validate(@RequestBody ValidateRequest request) {
        boolean valid = tokenUseCase.validateToken(request.getToken(), request.getClientSecret());
        return ResponseEntity.ok(new ValidateResponse(valid));
    }
}
