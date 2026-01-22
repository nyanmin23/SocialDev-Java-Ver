package dev.jade.socialdev.controller;

import dev.jade.socialdev.model.Message;
import dev.jade.socialdev.service.contract.MessageService;
import dev.jade.socialdev.service.contract.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/messages")
public class MessageRestController {

    private final MessageService messageService;
    private final UserService userService;

    @GetMapping("/public")
    public List<Message> displayPublicMessages() {
        return messageService.getPublicMessages();
    }

    @GetMapping("/private/{recipientId}")
    public List<Message> getConversation(
            @PathVariable Long recipientId,
            Principal principal
    ) {
        Long senderId = userService.getCurrentUserId(principal);
        return messageService.getPrivateMessagesForUser(senderId, recipientId);
    }
}

