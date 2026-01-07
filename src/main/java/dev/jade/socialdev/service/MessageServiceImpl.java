package dev.jade.socialdev.service;

import dev.jade.socialdev.entity.MessageEntity;
import dev.jade.socialdev.entity.UserEntity;
import dev.jade.socialdev.model.Message;
import dev.jade.socialdev.repository.MessageRepository;
import dev.jade.socialdev.repository.UserRepository;
import dev.jade.socialdev.service.contract.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public Message handleMessage(Message message) {
        UserEntity sender = userRepository
                .findByUsername(message.getSenderName())
                .orElseThrow(() -> new RuntimeException("User not found."));

        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSender(sender);
        messageEntity.setContent(message.getContent());
        messageEntity.setCreatedAt(Instant.now());
        MessageEntity saved = messageRepository.save(messageEntity);

        Message response = new Message();
        response.setSenderName(saved.getSender().getUsername());
        response.setContent(saved.getContent());
        response.setDate(saved.getCreatedAt());

        return response;
    }

    public List<Message> getMessages() {
        return messageRepository.findAllByOrderByCreatedAtAsc()
                .stream()
                .map(entity -> {
                    Message message = new Message();
                    message.setSenderName(entity.getSender().getUsername());
                    message.setContent(entity.getContent());
                    message.setDate(entity.getCreatedAt());
                    return message;
                })
                .toList();
    }
}
