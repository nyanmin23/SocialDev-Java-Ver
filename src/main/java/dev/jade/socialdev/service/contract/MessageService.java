package dev.jade.socialdev.service.contract;

import dev.jade.socialdev.model.Message;

import java.util.List;

public interface MessageService {
    Message handleMessage(Message message);

    List<Message> getMessages();
}
