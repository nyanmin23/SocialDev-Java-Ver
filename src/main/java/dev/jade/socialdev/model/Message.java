package dev.jade.socialdev.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Message {
    private String senderName;
    private Long userId;
    private String recipientName;
    private Long recipientId;
    private String content;
    private Instant date;
    private Status status;
}
