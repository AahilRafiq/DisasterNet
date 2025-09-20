package org.nitk.authservice.controller;

import org.jooq.DSLContext;
import org.jooq.Record1;
import org.nitk.authservice.dto.UserDTO;
import org.nitk.authservice.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @Value("${app.jwt.secret}")
    private String jwtSecret;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<String> signUp(@RequestBody UserDTO user) {
        String msg = authService.signup(user);
        Long userId = authService.findUserIdByEmail(user.getEmail());
        if (userId != null) {
            String token = createJwt(userId, user.getEmail());
            ResponseCookie cookie = buildJwtCookie(token);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(msg);
        }
        return ResponseEntity.ok(msg);
    }

    @PostMapping("/signin")
    public ResponseEntity<String> signIn(@RequestBody UserDTO user) {
        Long userId = authService.findUserIdByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userId != null) {
            String token = createJwt(userId, user.getEmail());
            ResponseCookie cookie = buildJwtCookie(token);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body("Signin successful");
        }
        return ResponseEntity.status(401).body("Invalid credentials");
    }

    private String createJwt(Long userId, String email) {
        com.auth0.jwt.algorithms.Algorithm alg = com.auth0.jwt.algorithms.Algorithm.HMAC256(jwtSecret);
        return com.auth0.jwt.JWT.create()
                .withClaim("id", userId)
                .withClaim("email", email)
                .sign(alg);
    }

    private ResponseCookie buildJwtCookie(String token) {
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .path("/")
                .build();
    }
}
