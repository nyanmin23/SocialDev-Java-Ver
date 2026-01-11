package dev.jade.socialdev.service.contract;

import dev.jade.socialdev.model.User;

import java.security.Principal;
import java.util.List;

public interface UserService {

    public Long getCurrentUserId(Principal principal);

    public List<User> getAllUsers();
}
