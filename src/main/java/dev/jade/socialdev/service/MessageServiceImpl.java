package dev.jade.socialdev.service;

import dev.jade.socialdev.entity.MessageEntity;
import dev.jade.socialdev.entity.UserEntity;
import dev.jade.socialdev.exception.UserNotFoundException;
import dev.jade.socialdev.model.IncomingMessage;
import dev.jade.socialdev.model.Message;
import dev.jade.socialdev.repository.MessageRepository;
import dev.jade.socialdev.repository.UserRepository;
import dev.jade.socialdev.service.contract.MessageService;
import dev.jade.socialdev.utils.MessageMapper;
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
    public Message handleMessage(Long userId, IncomingMessage incomingMessage) {
        String normalizedContent = validateMessage(incomingMessage);

        UserEntity sender = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("Sender not found: " + userId));

        UserEntity recipient = null;
        if (incomingMessage.getRecipientId() != null) {
            Long recipientId = incomingMessage.getRecipientId();
            recipient = userRepository.findById(recipientId)
                    .orElseThrow(() -> new UserNotFoundException("Recipient not found: " + recipientId));
        }

        MessageEntity saved = messageRepository.save(
                createMessageEntity(sender, recipient, normalizedContent)
        );

        return MessageMapper.mapToMessage(saved);
    }

    @Override
    public List<Message> getPublicMessages() {
        return messageRepository.findAllByRecipientIsNullOrderByCreatedAtAsc()
                .stream()
                .map(MessageMapper::mapToMessage)
                .toList();
    }

    @Override
    public List<Message> getPrivateMessagesForUser(Long senderId, Long recipientId) {
        return messageRepository
                .findAllBySender_IdAndRecipient_IdOrSender_IdAndRecipient_IdOrderByCreatedAtAsc(
                        senderId, recipientId,
                        recipientId, senderId
                )
                .stream()
                .map(MessageMapper::mapToMessage)
                .toList();
    }

    private MessageEntity createMessageEntity(
            UserEntity sender,
            UserEntity recipient,
            String content
    ) {
        MessageEntity messageEntity = new MessageEntity();
        messageEntity.setSender(sender);
        messageEntity.setRecipient(recipient);
        messageEntity.setContent(content);
        messageEntity.setCreatedAt(Instant.now());

        return messageEntity;
    }

    private String validateMessage(IncomingMessage incomingMessage) {
        String content = incomingMessage.getContent();
        if (content == null) {
            throw new IllegalArgumentException("Message content must not be empty");
        }

        content = content.trim();
        if (content.isEmpty()) {
            throw new IllegalArgumentException("Message content must not be empty");
        }

        int maxContentLength = 1000;
        if (content.length() > maxContentLength) {
            throw new IllegalArgumentException(
                    "Message length can't exceed " + maxContentLength + " characters"
            );
        }

        return content;
    }

}
