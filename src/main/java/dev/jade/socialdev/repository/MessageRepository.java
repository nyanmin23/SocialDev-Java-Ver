package dev.jade.socialdev.repository;

import dev.jade.socialdev.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    List<MessageEntity> findAllByRecipientIsNullOrderByCreatedAtAsc();

    List<MessageEntity> findAllBySender_IdAndRecipient_IdOrSender_IdAndRecipient_IdOrderByCreatedAtAsc(
            Long senderId, Long recipientId,
            Long recipientAsSender, Long senderAsRecipient
    );

}
