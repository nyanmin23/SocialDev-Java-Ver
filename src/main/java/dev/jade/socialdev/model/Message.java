package dev.jade.socialdev.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Message {
    private Long messageId;
    private Long senderId;
    private String senderName;
    private Long recipientId;
    private String recipientName;
    private String content;
    private Instant createdAt;
}
