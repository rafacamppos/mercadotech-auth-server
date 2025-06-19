package com.mercadotech.authserver.adapter;

import com.mercadotech.authserver.adapter.dto.LoginRequest;
import com.mercadotech.authserver.adapter.dto.TokenResponse;
import com.mercadotech.authserver.adapter.dto.ValidateRequest;
import com.mercadotech.authserver.adapter.dto.ValidateResponse;
import com.mercadotech.authserver.adapter.mapper.CredentialsMapper;
import com.mercadotech.authserver.adapter.mapper.TokenMapper;
import com.mercadotech.authserver.adapter.mapper.TokenResponseMapper;
import com.mercadotech.authserver.application.useCase.TokenUseCase;
import com.mercadotech.authserver.domain.model.Credentials;
import com.mercadotech.authserver.domain.model.TokenData;
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
        Credentials credentials = CredentialsMapper.from(request);
        TokenData tokenData = tokenUseCase.generateToken(credentials);
        TokenResponse response = TokenResponseMapper.from(tokenData);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/token/validate")
    public ResponseEntity<ValidateResponse> validate(@RequestBody ValidateRequest request) {
        Credentials credentials = CredentialsMapper.from(request);
        TokenData tokenData = TokenMapper.from(request);
        boolean valid = tokenUseCase.validateToken(tokenData, credentials);
        return ResponseEntity.ok(ValidateResponse.builder().valid(valid).build());
    }
}
