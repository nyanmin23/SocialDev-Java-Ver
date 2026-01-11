package dev.jade.socialdev.service;

import dev.jade.socialdev.exception.UnauthorizedAccessException;
import dev.jade.socialdev.exception.UserNotFoundException;
import dev.jade.socialdev.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    public Long getCurrentUserId(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedAccessException("User must be logged in.");
        }

        String username = principal.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username))
                .getId();
    }
}
