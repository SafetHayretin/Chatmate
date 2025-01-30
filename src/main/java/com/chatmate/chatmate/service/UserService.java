package com.chatmate.chatmate.service;

import com.chatmate.chatmate.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.chatmate.chatmate.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<User> searchUsers(String query) {
        List<User> result = new ArrayList<>();

        // Търси по username
        userRepository.findByUsernameIgnoreCase(query).ifPresent(result::add);

        // Търси по email, ако вече не е добавен
        userRepository.findByEmailIgnoreCase(query).ifPresent(user -> {
            if (!result.contains(user)) {
                result.add(user);
            }
        });

        return result;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElse(null);
    }
}

