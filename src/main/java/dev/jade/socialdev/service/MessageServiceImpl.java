package dev.jade.socialdev.service;

import dev.jade.socialdev.entity.MessageEntity;
import dev.jade.socialdev.entity.UserEntity;
import dev.jade.socialdev.exception.UserNotFoundException;
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
        UserEntity sender = findUserByUsernameOrThrow(message.getSenderName());

        MessageEntity saved = messageRepository.save(createMessageEntity(sender, message.getContent()));

        return mapToMessage(saved);
    }

    private UserEntity findUserByUsernameOrThrow(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UserNotFoundException("User not found: " + username));
    }

    private MessageEntity createMessageEntity(UserEntity userEntity, String content) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSender(userEntity);
        messageEntity.setContent(content);
        messageEntity.setCreatedAt(Instant.now());

        return messageEntity;
    }

    private Message mapToMessage(MessageEntity messageEntity) {
        Message message = new Message();
        message.setSenderName(messageEntity.getSender().getUsername());
        message.setContent(messageEntity.getContent());
        message.setDate(messageEntity.getCreatedAt());

        return message;
    }

    public List<Message> getMessages() {
        return messageRepository.findAllByOrderByCreatedAtAsc()
                .stream()
                .map(this::mapToMessage)
                .toList();
    }
}
