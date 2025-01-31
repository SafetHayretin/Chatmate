package com.chatmate.chatmate.service;

import com.chatmate.chatmate.dto.ChannelDto;
import com.chatmate.chatmate.dto.UserDTO;
import com.chatmate.chatmate.entity.Channel;
import com.chatmate.chatmate.entity.ChannelRole;
import com.chatmate.chatmate.entity.RoleName;
import com.chatmate.chatmate.entity.User;
import com.chatmate.chatmate.repository.ChannelRepository;
import com.chatmate.chatmate.repository.ChannelRoleRepository;
import com.chatmate.chatmate.repository.FriendshipRepository;
import com.chatmate.chatmate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ChannelService {
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRoleRepository channelRoleRepository;

    @Autowired
    private FriendshipRepository friendshipRepository;


    public ChannelDto createChannel(Long userId, String channelName) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        if (channelRepository.existsByName(channelName)) {
            throw new RuntimeException("Channel name already exists");
        }

        Channel channel = new Channel();
        channel.setName(channelName);
        channel.setOwner(user);
        channel = channelRepository.save(channel);

        ChannelRole ownerRole = new ChannelRole();
        ownerRole.setUser(user);
        ownerRole.setChannel(channel);
        ownerRole.setRole(RoleName.OWNER);

        channelRoleRepository.save(ownerRole);

        return new ChannelDto(channel, RoleName.OWNER.toString());
    }

    public List<UserDTO> getUsersNotInChannel(Long userId, Long channelId) {
        List<User> friends = friendshipRepository.findFriendsByUserId(userId);

        List<Long> usersInChannel = channelRoleRepository.findByChannelId(channelId)
                .stream()
                .map(channelRole -> channelRole.getUser().getId())
                .collect(Collectors.toList());

        return friends.stream()
                .filter(friend -> !usersInChannel.contains(friend.getId()))
                .map(friend -> new UserDTO(friend.getId(), friend.getUsername()))
                .collect(Collectors.toList());
    }

    public List<ChannelDto> getAllChannels() {
        return channelRepository.findByDeletedFalse().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ChannelDto> getChannelsByUser(Long userId) {
        List<ChannelRole> userRoles = channelRoleRepository.findByUserIdAndChannelNotDeleted(userId);

        return userRoles.stream()
                .map(role -> new ChannelDto(
                        role.getChannel(),
                        role.getRole().toString()
                ))
                .collect(Collectors.toList());
    }

    private ChannelDto convertToDto(Channel channel) {
        return new ChannelDto(channel);
    }

    public List<UserDTO> getUsersInChannel(Long channelId) {
        List<ChannelRole> channelRoles = channelRoleRepository.findByChannelId(channelId);

        return channelRoles.stream()
                .map(channelRole -> new UserDTO(
                        channelRole.getUser().getId(),
                        channelRole.getUser().getUsername(),
                        channelRole.getRole().name()))
                .collect(Collectors.toList());
    }

    public ChannelDto renameChannel(Long userId, Long channelId, String newName) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found"));

        if (!channel.getOwner().getId().equals(userId)) {
            throw new RuntimeException("Unauthorized: Only Owner or Admin can rename the channel.");
        }

        channel.setName(newName);
        channelRepository.save(channel);

        return new ChannelDto(channel.getId(), channel.getName());
    }

    public void deleteChannel(Long id, Long userId) {
        Optional<Channel> channel = channelRepository.findById(id);
        if (channel.isEmpty()) {
            throw new RuntimeException("Channel not found");
        }

        boolean isOwner = channelRoleRepository.existsByChannelIdAndUserIdAndRole(id, userId, RoleName.OWNER);
        if (!isOwner) {
            throw new RuntimeException("Only OWNER can delete channel.");
        }

        Channel channelToDelete = channel.get();
        channelToDelete.setDeleted(true);
        channelRepository.save(channelToDelete);
    }

}

