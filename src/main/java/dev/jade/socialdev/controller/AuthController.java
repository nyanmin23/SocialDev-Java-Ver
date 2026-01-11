package dev.jade.socialdev.controller;

import dev.jade.socialdev.model.AuthRequest;
import dev.jade.socialdev.model.User;
import dev.jade.socialdev.service.contract.AuthService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/register")
    public User registerUser(@Valid @RequestBody AuthRequest request, HttpSession session) {
        User user = authService.register(request.username(), request.password());
        authenticateUser(user, session);
        return user;
    }

    @PostMapping("/login")
    public User loginUser(@Valid @RequestBody AuthRequest request, HttpSession session) {
        User user = authService.login(request.username(), request.password());
        authenticateUser(user, session);
        return user;
    }

    private void authenticateUser(User user, HttpSession session) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(
                        user.getUsername(),
                        null,
                        Collections.emptyList()
                );

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();
        securityContext.setAuthentication(authentication);
        SecurityContextHolder.setContext(securityContext);

        session.setAttribute(
                HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                securityContext
        );
        session.setAttribute("userId", user.getUserId());
        session.setAttribute("username", user.getUsername());
    }
}
