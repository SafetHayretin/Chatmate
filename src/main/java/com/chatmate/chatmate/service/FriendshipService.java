package com.chatmate.chatmate.service;

import com.chatmate.chatmate.dto.UserDTO;
import com.chatmate.chatmate.entity.Friendship;
import com.chatmate.chatmate.entity.RoleName;
import com.chatmate.chatmate.entity.User;
import com.chatmate.chatmate.repository.FriendshipRepository;
import com.chatmate.chatmate.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public FriendshipService(FriendshipRepository friendshipRepository, UserRepository userRepository) {
        this.friendshipRepository = friendshipRepository;
        this.userRepository = userRepository;
    }

    // Метод за добавяне на приятел
    public Friendship addFriend(Long userId, Long friendId) {
        // Проверка дали потребителите съществуват
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));
        User friend = userRepository.findById(friendId)
                .orElseThrow(() -> new RuntimeException("Friend not found with ID: " + friendId));

        // Проверка дали вече са приятели
        boolean alreadyFriends = friendshipRepository.existsByUserAndFriend(user, friend);
        if (alreadyFriends) {
            throw new RuntimeException("Users are already friends");
        }

        // Създаване на приятелство
        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setFriend(friend);

        return friendshipRepository.save(friendship);
    }

    // Метод за връщане на списък с приятели на потребител
    public List<UserDTO> getFriends(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        return friendshipRepository.findAllByUser(user).stream()
                .map(friendship -> new UserDTO(friendship.getFriend().getId(), friendship.getFriend().getUsername(), RoleName.GUEST.name()))
                .collect(Collectors.toList());
    }
}
