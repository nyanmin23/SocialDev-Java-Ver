package dev.jade.socialdev.utils;

import dev.jade.socialdev.entity.UserEntity;
import dev.jade.socialdev.model.User;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UserMapper {
    public static User mapToUser(UserEntity entity) {
        User user = new User();
        user.setUserId(entity.getId());
        user.setUsername(entity.getUsername());
        user.setCreatedAt(entity.getCreatedAt());
        return user;
    }
}