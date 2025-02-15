package com.chatmate.chatmate.repository;
import com.chatmate.chatmate.entity.Channel;
import com.chatmate.chatmate.entity.ChannelRole;
import com.chatmate.chatmate.entity.RoleName;
import com.chatmate.chatmate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ChannelRoleRepository extends JpaRepository<ChannelRole, Long> {
    Optional<ChannelRole> findByUserAndChannel(User user, Channel channel);

    List<ChannelRole> findByChannelId(Long channelId);

    boolean existsByUserIdAndChannelIdAndRole(Long ownerId, Long channelId, RoleName role);

    Optional<ChannelRole> findByUserIdAndChannelIdAndRole(Long guestUserId, Long channelId, RoleName role);

    @Query("SELECT cr FROM ChannelRole cr WHERE cr.user.id = :userId AND cr.channel.deleted = false")
    List<ChannelRole> findByUserIdAndChannelNotDeleted(@Param("userId") Long userId);

    boolean existsByChannelIdAndUserIdAndRole(Long channelId, Long userId, RoleName role);
}

