package dev.jade.socialdev.controller;

import dev.jade.socialdev.model.Message;
import dev.jade.socialdev.service.contract.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class MessageWebSocketController {
    // MessageMapping -
    // https://stackoverflow.com/questions/52999004/subscribemapping-vs-messagemapping


    // This is apparently used for sending message to specific user.
    private final SimpMessagingTemplate simpMessagingTemplate;
    private final MessageService messageService;

    // react only to the SEND messages with the destination having prefix /app and matching the topic set in the annotation.
    @MessageMapping("/message")
    // send to specified destination (if message contains destination, it will take precedence over this specified destination)
    @SendTo("/topic/public")
    public Message sendMessage(@Payload Message message) {
        return messageService.handleMessage(message);
    }

    @MessageMapping("/private-message")
    public Message sendPrivateMessage(@Payload Message message) {
        simpMessagingTemplate.convertAndSendToUser(
                message.getRecipientName(),
                "/queue/private",
                message);

        return message;
    }
}
