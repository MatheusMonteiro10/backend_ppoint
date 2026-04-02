package com.ppoint.backend.service;

import com.ppoint.backend.domain.User;
import com.ppoint.backend.repository.UserRepository;
import org.aspectj.weaver.NewConstructorTypeMunger;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository repository;
    private final EncryptionService crypto;
    private final JwtService jwtService;
    private final GoogleAuthService googleAuthService;

    public AuthService(UserRepository repository, EncryptionService crypto, JwtService jwtService, GoogleAuthService googleAuthService) {
        this.repository = repository;
        this.crypto = crypto;
        this.jwtService = jwtService;
        this.googleAuthService = googleAuthService;
    }

    public void register(String name, String email, String password) {
        if (repository.findByEmail(email).isPresent()) {
            throw new RuntimeException("Email já cadastrado");
        }

        User user = new User();
        //user.setId(UUID.randomUUID());
        //setId desnecessário com a geração automática via Hibernate
        user.setInstagramUser(name);
        user.setEmail(email);
        user.setPassword(crypto.encrypt(password));
        user.setRole("USER");

        repository.save(user);
    }

    public String login(String email, String password) {
        User user = repository.findByEmail(email).orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (!crypto.compare(password, user.getPassword())) {
            throw new RuntimeException("Senha inválida");
        }

        return jwtService.generateToken(user.getEmail());
    }

    public String googleAuth(String token) {
        var payload = googleAuthService.verify(token);

        if (payload == null) {
            throw new RuntimeException("Token inválido");
        }

        if (!payload.getEmailVerified()) {
            throw new RuntimeException("Email não verificado");
        }

        String email = payload.getEmail();
        String name = (String) payload.get("name");
        String googleId = payload.getSubject();
        String picture = (String) payload.get("picture");

        User user = repository.findByEmail(email).map(existing -> {
            // Se já existe, opcional vincular conta
            if (existing.getGoogleId() == null) {
                existing.setGoogleId(googleId);
                existing.setProvider("GOOGLE");
                return repository.save(existing);
            }
            return existing;
            }).orElseGet(()-> {
            // Cadastro automático
            User newUser = new User();
            newUser.setEmail(email);
            newUser.setGoogleId(googleId);
            newUser.setProvider("GOOGLE");
            newUser.setPicture(picture);

            return repository.save(newUser);
        });

        return jwtService.generateToken(user.getEmail());
    }
}