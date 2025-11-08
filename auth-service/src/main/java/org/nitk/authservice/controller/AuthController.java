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
    public ResponseEntity<UserDTO> signUp(@RequestBody UserDTO user) {
        Long userId = authService.signup(user);
        if (userId != null) {
            UserDTO created = authService.getUserById(userId);
            String token = createJwt(userId, created.getEmail(), created.getRole().name());
            ResponseCookie cookie = buildJwtCookie(token);
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(created);
        }
        return ResponseEntity.badRequest().build();
    }

    @PostMapping("/signin")
    public ResponseEntity<UserDTO> signIn(@RequestBody UserDTO user) {
        Long userId = authService.findUserIdByEmailAndPassword(user.getEmail(), user.getPassword());
        if (userId != null) {
            UserDTO dto = authService.getUserById(userId);
            String token = createJwt(userId, dto.getEmail(), dto.getRole().name());
            ResponseCookie cookie = buildJwtCookie(token);
            System.out.println("User " + dto.getEmail() + " signed in with role " + dto.getRole().name());
            return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString()).body(dto);
        }
        return ResponseEntity.status(401).build();
    }

    private String createJwt(Long userId, String email, String role) {
        com.auth0.jwt.algorithms.Algorithm alg = com.auth0.jwt.algorithms.Algorithm.HMAC256(jwtSecret);
        return com.auth0.jwt.JWT.create()
                .withClaim("id", userId)
                .withClaim("email", email)
                .withClaim("role", role)
                .sign(alg);
    }

    private ResponseCookie buildJwtCookie(String token) {
        return ResponseCookie.from("jwt", token)
                .httpOnly(true)
                .path("/")
                .build();
    }
}
