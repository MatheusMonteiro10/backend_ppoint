package com.ppoint.backend.controller;

import com.google.api.client.auth.openidconnect.IdToken;
import com.google.api.client.auth.openidconnect.IdTokenResponse;
import com.ppoint.backend.dto.GoogleTokenDTO;
import com.ppoint.backend.service.AuthService;
import com.ppoint.backend.dto.LoginDTO;
import com.ppoint.backend.dto.RegisterDTO;
import com.ppoint.backend.service.GoogleAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/*Passo a passo para teste:
1 - Registro de novo usuário:
    POST http://localhost:8080/auth/register
    Em Body adicionar:
    {
        "name": "Matheus",
        "email": "matheus@email.com",
        "password": "123456"
    }

    Resposta esperada: 200 ok
    Usuário registrado no banco de dados

2 - Login do usuário:
    POST http://localhost:8080/auth/login

    Resposta esperada: 200 ok junto com token do usuário

3 - GET http://localhost:8080/test
    Em Authorization marcar como Bearer token
    em seguida colar 'token do usuário' no campo de token

    Reposta esperada: API rodando!
*/

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public  AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public String register(@RequestBody RegisterDTO dto) {
        authService.register(dto.name(), dto.email(), dto.password());
        return "Usuário criado com sucesso";
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginDTO dto) {
        return authService.login(dto.email(), dto.password());
    }

    @PostMapping("/google")
    public ResponseEntity<String> googleLogin(@RequestBody GoogleTokenDTO dto) {
        return ResponseEntity.ok(authService.googleAuth(dto.token()));
    }
}