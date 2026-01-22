package dev.jade.socialdev.service;

import dev.jade.socialdev.entity.UserEntity;
import dev.jade.socialdev.exception.UnauthorizedAccessException;
import dev.jade.socialdev.exception.UserNotFoundException;
import dev.jade.socialdev.model.User;
import dev.jade.socialdev.repository.UserRepository;
import dev.jade.socialdev.service.contract.UserService;
import dev.jade.socialdev.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    /**
     * Gets the current user's ID from the security principal.
     *
     * @param principal the security principal
     * @return the current user's ID
     * @throws UnauthorizedAccessException if principal is null
     * @throws UserNotFoundException       if user is not found
     */
    public Long getCurrentUserId(Principal principal) {
        if (principal == null) {
            throw new UnauthorizedAccessException("User must be logged in.");
        }

        String username = principal.getName();
        return findByUsernameOrThrow(username).getId();
    }

    public UserEntity findByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(UserMapper::mapToUser)
                .toList();
    }
}
