package com.mercadotech.authserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServerApplication {
    public static void main(String[] args) {
        System.out.println("Hello World");
        SpringApplication.run(AuthServerApplication.class, args);
    }
}
