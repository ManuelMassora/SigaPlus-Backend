package com.sigaplus.sigaplus.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@Configuration
public class UtilsConfig {
    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public JwtConfig(/* Remova KeyLoader keyLoader do construtor se for a causa do ciclo */) throws Exception {

        // Crie uma nova instância ou use um método estático
        KeyLoader keyLoader = new KeyLoader(); // <--- Assumindo que KeyLoader pode ser instanciado

        this.publicKey = keyLoader.loadPublicKey("classpath:keypub.pem");
        this.privateKey = keyLoader.loadPrivateKey("classpath:newkey.pri.pem");
    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}