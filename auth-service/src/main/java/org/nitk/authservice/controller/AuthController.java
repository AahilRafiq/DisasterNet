package org.nitk.authservice.controller;

import org.nitk.authservice.dto.UserDTO;
import org.nitk.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDTO user) {
        String msg = authService.signup(user);
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signin(@RequestBody UserDTO user) {
        String msg = authService.signin(user);
        return ResponseEntity.ok(msg);
    }
}

