package dev.jade.socialdev.repository;

import dev.jade.socialdev.entity.MessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<MessageEntity, Long> {

    /**
     * Finds all public messages (where recipient is null) ordered by creation time.
     *
     * @return list of public messages
     */
    List<MessageEntity> findAllByRecipientIsNullOrderByCreatedAtAsc();

    /**
     * Finds all private messages between two users in both directions.
     *
     * @param userId1 first user ID
     * @param userId2 second user ID
     * @return list of messages between the users
     */
    @Query("""
            SELECT m FROM MessageEntity m
            WHERE (m.sender.id = :userId1 AND m.recipient.id = :userId2)
               OR (m.sender.id = :userId2 AND m.recipient.id = :userId1)
            ORDER BY m.createdAt ASC
            """)
    List<MessageEntity> findPrivateMessagesBetweenUsers(
            @Param("userId1") Long userId1,
            @Param("userId2") Long userId2
    );
}
