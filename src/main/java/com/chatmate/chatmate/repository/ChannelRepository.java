package com.chatmate.chatmate.repository;

import com.chatmate.chatmate.entity.Channel;
import com.chatmate.chatmate.entity.ChannelRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChannelRepository extends JpaRepository<Channel, Long> {
    boolean existsByName(String channelName);
}
