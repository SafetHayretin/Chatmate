package com.chatmate.chatmate.repository;

import com.chatmate.chatmate.entity.Channel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    boolean existsByName(String channelName);

    List<Channel> findByDeletedFalse();
}
