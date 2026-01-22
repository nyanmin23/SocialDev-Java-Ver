package dev.jade.socialdev.service;

import dev.jade.socialdev.entity.UserEntity;
import dev.jade.socialdev.exception.InvalidCredentialsException;
import dev.jade.socialdev.exception.UsernameAlreadyTakenException;
import dev.jade.socialdev.model.User;
import dev.jade.socialdev.repository.UserRepository;
import dev.jade.socialdev.service.contract.AuthService;
import dev.jade.socialdev.service.contract.UserService;
import dev.jade.socialdev.util.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;

    @Override
    public User register(String username, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new UsernameAlreadyTakenException(username);
        }

        UserEntity saved = userRepository.save(createUserEntity(username, password));
        return UserMapper.mapToUser(saved);
    }

    @Override
    public User login(String username, String password) {
        UserEntity userEntity = userService.findByUsernameOrThrow(username);

        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new InvalidCredentialsException("Invalid password");
        }

        return UserMapper.mapToUser(userEntity);
    }

    /**
     * Creates a new UserEntity with encoded password.
     *
     * @param username the username
     * @param password the raw password to encode
     * @return the created UserEntity
     */
    private UserEntity createUserEntity(String username, String password) {
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        userEntity.setPassword(passwordEncoder.encode(password));
        userEntity.setCreatedAt(Instant.now());

        return userEntity;
    }
}
