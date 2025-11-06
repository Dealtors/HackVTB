package com.orchestra.api.controller.auth;

import com.orchestra.api.dto.request.LoginRequest;
import com.orchestra.api.dto.request.RegisterRequest;
import com.orchestra.api.dto.response.UserResponse;
import com.orchestra.api.entity.UserEntity;
import com.orchestra.api.repository.user.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

// AuthController.java
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final UserRepository users;
    private final PasswordEncoder encoder;
    private final AuthenticationManager authManager;

    public AuthController(UserRepository users, PasswordEncoder encoder, AuthenticationConfiguration cfg) throws Exception {
        this.users = users;
        this.encoder = encoder;
        this.authManager = cfg.getAuthenticationManager();
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequest req) {
        if (req.name() == null || req.email() == null || req.password() == null || req.role() == null)
            return ResponseEntity.badRequest().body("Missing fields");
        if (!req.password().equals(req.confirmPassword()))
            return ResponseEntity.badRequest().body("Passwords do not match");
        if (users.findByEmail(req.email()).isPresent())
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already exists");
        UserEntity u = new UserEntity();
        u.setName(req.name());
        u.setEmail(req.email().toLowerCase());
        u.setPassword(encoder.encode(req.password()));
        u.setRole(req.role().toUpperCase());
        users.save(u);
        return ResponseEntity.ok(new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole()));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        Authentication auth = authManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.email(), req.password()));
        SecurityContextHolder.getContext().setAuthentication(auth);
        UserEntity u = users.findByEmail(req.email()).orElseThrow();
        return ResponseEntity.ok(new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole()));
    }

    @GetMapping("/me")
    public ResponseEntity<?> me() {
        Authentication a = SecurityContextHolder.getContext().getAuthentication();
        if (a == null || !a.isAuthenticated() || "anonymousUser".equals(a.getPrincipal()))
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        String email = a.getName();
        UserEntity u = users.findByEmail(email).orElseThrow();
        return ResponseEntity.ok(new UserResponse(u.getId(), u.getName(), u.getEmail(), u.getRole()));
    }
}
