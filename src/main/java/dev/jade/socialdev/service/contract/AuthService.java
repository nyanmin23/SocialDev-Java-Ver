package dev.jade.socialdev.service.contract;

import dev.jade.socialdev.model.User;

public interface AuthService {

    User login(String username, String password);

    User register(String username, String password);
}
