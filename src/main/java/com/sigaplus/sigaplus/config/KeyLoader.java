package com.sigaplus.sigaplus.config;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Key;
import java.security.KeyFactory;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Component
public class KeyLoader {

    private final ResourceLoader resourceLoader;

    public KeyLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public RSAPublicKey loadPublicKey(String path) throws Exception {
        return (RSAPublicKey) loadKey(path, true);
    }

    public RSAPrivateKey loadPrivateKey(String path) throws Exception {
        return (RSAPrivateKey) loadKey(path, false);
    }

    private KeyFactory getKeyFactory() throws Exception {
        return KeyFactory.getInstance("RSA");
    }

    private Key loadKey(String path, boolean isPublic) throws Exception {
        Resource resource = resourceLoader.getResource(path);
        byte[] keyBytes = Files.readAllBytes(Paths.get(resource.getURI()));

        // Converte para String e remove os cabeçalhos e espaços extras
        var keyContent = new String(keyBytes, StandardCharsets.UTF_8)
                .replaceAll("-----BEGIN (?:RSA )?(PUBLIC|PRIVATE) KEY-----", "")
                .replaceAll("-----END (?:RSA )?(PUBLIC|PRIVATE) KEY-----", "")
                .replaceAll("\\s+", "");

        byte[] decodedKey = Base64.getDecoder().decode(keyContent);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");

        if (isPublic) {
            return keyFactory.generatePublic(new X509EncodedKeySpec(decodedKey));
        } else {
            return keyFactory.generatePrivate(new PKCS8EncodedKeySpec(decodedKey));
        }
    }
}