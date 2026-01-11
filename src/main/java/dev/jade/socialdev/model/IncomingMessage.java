package dev.jade.socialdev.model;

import lombok.Data;

@Data
public class IncomingMessage {
    private String content;
    private Long recipientId;
}
