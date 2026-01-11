package dev.jade.socialdev.utils;

import dev.jade.socialdev.entity.MessageEntity;
import dev.jade.socialdev.model.Message;
import lombok.experimental.UtilityClass;

@UtilityClass
public final class MessageMapper {

    public static Message mapToMessage(MessageEntity messageEntity) {
        Message message = new Message();
        message.setMessageId(messageEntity.getId());
        message.setSenderId(messageEntity.getSender().getId());
        message.setSenderName(messageEntity.getSender().getUsername());

        if (messageEntity.getRecipient() != null) {
            message.setRecipientName(messageEntity.getRecipient().getUsername());
            message.setRecipientId(messageEntity.getRecipient().getId());
        }
        message.setContent(messageEntity.getContent());
        message.setCreatedAt(messageEntity.getCreatedAt());
        return message;
    }

}
