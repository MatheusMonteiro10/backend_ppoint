package com.ppoint.backend.controller;

import com.ppoint.backend.domain.User;
import com.ppoint.backend.service.AuthService;
import com.ppoint.backend.dto.LoginDTO;
import com.ppoint.backend.dto.RegisterDTO;
import org.springframework.web.bind.annotation.*;

//url: http://localhost:8080/test

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService service;

    public  AuthController(AuthService service) {
        this.service = service;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterDTO dto) {
        service.register(dto.name(), dto.email(), dto.password());
        return "Usuário criado com sucesso";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO dto) {
        return service.login(dto.email(), dto.password());
    }
}