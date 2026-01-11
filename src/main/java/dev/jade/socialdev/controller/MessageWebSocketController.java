package dev.jade.socialdev.controller;

import dev.jade.socialdev.model.IncomingMessage;
import dev.jade.socialdev.model.Message;
import dev.jade.socialdev.service.UserService;
import dev.jade.socialdev.service.contract.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {
    // MessageMapping -
    // https://stackoverflow.com/questions/52999004/subscribemapping-vs-messagemapping

    // This is apparently used for sending message to specific user.
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final UserService userService;

    @MessageMapping("/message")
    // react only to the SEND messages with the destination having prefix /app and matching the topic set in the annotation.
    @SendTo("/topic/public")
    // send to specified destination (if message contains destination, it will take precedence over this specified destination)
    public Message sendMessage(@Payload IncomingMessage incomingMessage, Principal principal) {
        Long senderId = userService.getCurrentUserId(principal);
        return messageService.handleMessage(senderId, incomingMessage);
    }

    @MessageMapping("/private-message")
    public Message sendPrivateMessage(@Payload IncomingMessage incomingMessage, Principal principal) {
        Long senderId = userService.getCurrentUserId(principal);
        String senderName = principal.getName();

        if (incomingMessage.getRecipientId() == null) {
            throw new IllegalArgumentException("recipientId can't be null for private messages");
        }

        Message saved = messageService.handleMessage(senderId, incomingMessage);

        simpMessagingTemplate.convertAndSendToUser(
                senderName,
                "/queue/private",
                saved
        );

        simpMessagingTemplate.convertAndSendToUser(
                saved.getRecipientName(),
                "/queue/private",
                saved
        );

        return saved;
    }
}
