package com.gelir.gider.controller;

import com.gelir.gider.dto.LoginRequest;
import com.gelir.gider.dto.LoginResponse;
import com.gelir.gider.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestHeader("Authorization") String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            authService.logout(token);
        }
        return ResponseEntity.ok("Çıkış başarılı");
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody com.gelir.gider.dto.RegisterRequest request) {
        try {
            com.gelir.gider.dto.UserDTO userDTO = com.gelir.gider.dto.UserDTO.builder()
                    .username(request.getUsername())
                    .password(request.getPassword())
                    .email(request.getEmail())
                    .fullName(request.getFullName())
                    .build();

            authService.register(userDTO);
            return ResponseEntity.ok("Kayıt başarılı. Giriş yapabilirsiniz.");
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
