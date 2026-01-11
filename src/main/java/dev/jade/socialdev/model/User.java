package dev.jade.socialdev.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.time.Instant;

@Data
public class User {
    private Long userId;

    private String username;

    // TODO: review how this annotation work
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private Instant createdAt;
}
