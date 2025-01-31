package com.chatmate.chatmate.service;

import com.chatmate.chatmate.entity.Channel;
import com.chatmate.chatmate.entity.ChannelRole;
import com.chatmate.chatmate.entity.RoleName;
import com.chatmate.chatmate.entity.User;
import com.chatmate.chatmate.repository.ChannelRepository;
import com.chatmate.chatmate.repository.ChannelRoleRepository;
import com.chatmate.chatmate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ChannelRoleService {
    @Autowired
    private ChannelRoleRepository channelRoleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    public ChannelRole addRoleToUserInChannel(Long userId, Long channelId, RoleName roleName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));

        Optional<ChannelRole> existingRole = channelRoleRepository.findByUserAndChannel(user, channel);
        if (existingRole.isPresent()) {
            ChannelRole role = existingRole.get();
            role.setRole(roleName);
            return channelRoleRepository.save(role);
        }

        ChannelRole newRole = new ChannelRole();
        newRole.setUser(user);
        newRole.setChannel(channel);
        newRole.setRole(roleName);

        return channelRoleRepository.save(newRole);
    }
    public void removeGuestFromChannel(Long ownerId, Long channelId, Long guestUserId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        boolean isOwner = channelRoleRepository.existsByUserIdAndChannelIdAndRole(ownerId, channelId, RoleName.OWNER);
        if (!isOwner) {
            throw new RuntimeException("Only the channel owner can remove users");
        }

        ChannelRole guestRole = channelRoleRepository.findByUserIdAndChannelIdAndRole(guestUserId, channelId, RoleName.GUEST)
                .orElseThrow(() -> new RuntimeException("User is not a guest in this channel"));

        channelRoleRepository.delete(guestRole);
    }
}
