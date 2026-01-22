package dev.jade.socialdev.service;

import dev.jade.socialdev.entity.MessageEntity;
import dev.jade.socialdev.entity.UserEntity;
import dev.jade.socialdev.exception.UserNotFoundException;
import dev.jade.socialdev.model.IncomingMessage;
import dev.jade.socialdev.model.Message;
import dev.jade.socialdev.repository.MessageRepository;
import dev.jade.socialdev.repository.UserRepository;
import dev.jade.socialdev.service.contract.MessageService;
import dev.jade.socialdev.util.MessageMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MessageServiceImpl implements MessageService {

    private static final int MAX_CONTENT_LENGTH = 1000;
    private static final String EMPTY_CONTENT_ERROR = "Message content must not be empty";

    private final MessageRepository messageRepository;
    private final UserRepository userRepository;

    @Override
    public Message handleMessage(Long userId, IncomingMessage incomingMessage) {
        String normalizedContent = validateMessage(incomingMessage);

        UserEntity sender = getUserByIdOrThrow(userId, "Sender");

        UserEntity recipient = null;
        if (incomingMessage.getRecipientId() != null) {
            recipient = getUserByIdOrThrow(incomingMessage.getRecipientId(), "Recipient");
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
                .findPrivateMessagesBetweenUsers(senderId, recipientId)
                .stream()
                .map(MessageMapper::mapToMessage)
                .toList();
    }

    /**
     * Creates a new MessageEntity.
     *
     * @param sender    the sender user
     * @param recipient the recipient user (null for public messages)
     * @param content   the message content
     * @return the created MessageEntity
     */
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

    /**
     * Validates and normalizes message content.
     *
     * @param incomingMessage the incoming message to validate
     * @return normalized content
     * @throws IllegalArgumentException if content is invalid
     */
    private String validateMessage(IncomingMessage incomingMessage) {
        String content = incomingMessage.getContent();

        if ((content == null) || (content.trim().isEmpty())) {
            throw new IllegalArgumentException(EMPTY_CONTENT_ERROR);
        }

        if (content.length() > MAX_CONTENT_LENGTH) {
            throw new IllegalArgumentException(
                    "Message length can't exceed " + MAX_CONTENT_LENGTH + " characters"
            );
        }
        return content;
    }

    private UserEntity getUserByIdOrThrow(Long userId, String role) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(role + " not found: " + userId));
    }

}
