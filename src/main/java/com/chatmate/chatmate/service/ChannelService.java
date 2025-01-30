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

        return new ChannelDto(channel);
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
        return channelRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public List<ChannelDto> getChannelsByUser(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return channelRoleRepository.findAllByUser(user).stream()
                .map(channelRole -> convertToDto(channelRole.getChannel())) // Convert to DTO
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
                        channelRole.getRole().name()))  // <-- Добавяме ролята
                .collect(Collectors.toList());
    }

    public void deleteChannel(Long channelId, Long userId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));

        ChannelRole ownerRole = channelRoleRepository.findByUserAndChannel(
                userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found")),
                channel
        ).orElseThrow(() -> new RuntimeException("User is not a member of the channel"));

        if (ownerRole.getRole() != RoleName.OWNER) {
            throw new RuntimeException("Only the channel owner can delete the channel");
        }

        channel.setDeleted(true);
        channelRepository.save(channel);
    }

}

