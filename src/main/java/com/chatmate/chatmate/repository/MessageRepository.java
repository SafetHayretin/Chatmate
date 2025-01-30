package com.chatmate.chatmate.repository;

import com.chatmate.chatmate.entity.Channel;
import com.chatmate.chatmate.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message, Long> {
    List<Message> findByChannelOrderByTimestampAsc(Channel channel);

    @Query("SELECT m FROM Message m WHERE " +
            "(m.sender.id = :userId1 AND m.receiver.id = :userId2) OR " +
            "(m.sender.id = :userId2 AND m.receiver.id = :userId1) " +
            "ORDER BY m.timestamp ASC")
    List<Message> findDirectMessages(@Param("userId1") Long userId1, @Param("userId2") Long userId2);


}
