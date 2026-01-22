package dev.jade.socialdev.controller;

import dev.jade.socialdev.model.IncomingMessage;
import dev.jade.socialdev.model.Message;
import dev.jade.socialdev.service.contract.MessageService;
import dev.jade.socialdev.service.contract.UserService;
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

    private static final String PUBLIC_TOPIC = "/topic/public";
    private static final String PRIVATE_QUEUE = "/queue/private";

    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;
    private final UserService userService;

    /**
     * Handles public messages sent to all connected users.
     *
     * @param incomingMessage the message from the client
     * @param principal       the authenticated user
     * @return the saved message broadcast to all subscribers
     */
    @MessageMapping("/message")
    @SendTo(PUBLIC_TOPIC)
    public Message sendMessage(@Payload IncomingMessage incomingMessage, Principal principal) {
        Long senderId = userService.getCurrentUserId(principal);
        return messageService.handleMessage(senderId, incomingMessage);
    }

    /**
     * Handles private messages sent between two users.
     *
     * @param incomingMessage the message from the client
     * @param principal       the authenticated user
     * @return the saved message
     */
    @MessageMapping("/private-message")
    public Message sendPrivateMessage(@Payload IncomingMessage incomingMessage, Principal principal) {
        Long senderId = userService.getCurrentUserId(principal);
        String senderName = principal.getName();

        Message saved = messageService.handleMessage(senderId, incomingMessage);

        simpMessagingTemplate.convertAndSendToUser(senderName, PRIVATE_QUEUE, saved);
        simpMessagingTemplate.convertAndSendToUser(saved.getRecipientName(), PRIVATE_QUEUE, saved);

        return saved;
    }
}
