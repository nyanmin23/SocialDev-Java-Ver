package dev.jade.socialdev.controller;

import dev.jade.socialdev.model.AuthRequest;
import dev.jade.socialdev.model.User;
import dev.jade.socialdev.service.contract.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public User registerUser(@RequestBody AuthRequest request) {
        return authService.register(request.username(), request.password());
    }

    @PostMapping("/login")
    public User loginUser(@RequestBody AuthRequest request) {
        return authService.login(request.username(), request.password());
    }
}
