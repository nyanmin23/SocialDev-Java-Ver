package dev.jade.socialdev.service.contract;

import dev.jade.socialdev.model.IncomingMessage;
import dev.jade.socialdev.model.Message;

import java.util.List;

public interface MessageService {
    Message handleMessage(Long userId, IncomingMessage incomingMessage);

    List<Message> getPublicMessages();

    List<Message> getPrivateMessagesForUser(Long senderId, Long recipientId);
}
