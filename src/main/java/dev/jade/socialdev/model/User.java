package dev.jade.socialdev.model;

import lombok.Data;

import java.time.Instant;

@Data
public class User {
    private Long userId;
    private String username;
    // private String email;
    private String password;
    private Instant createdAt;
}
