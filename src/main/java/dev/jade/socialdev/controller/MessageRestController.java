package dev.jade.socialdev.controller;

import dev.jade.socialdev.model.Message;
import dev.jade.socialdev.service.contract.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class MessageRestController {

    private final MessageService messageService;

    @GetMapping("messages")
    public List<Message> displayMessages() {
        return messageService.getMessages();
    }
}
