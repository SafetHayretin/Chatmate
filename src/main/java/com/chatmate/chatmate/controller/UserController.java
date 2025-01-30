package com.chatmate.chatmate.controller;

import com.chatmate.chatmate.entity.RoleName;
import com.chatmate.chatmate.entity.User;
import com.chatmate.chatmate.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<User>> searchUsers(@RequestParam String query) {
        List<User> users = userService.searchUsers(query);
        return ResponseEntity.ok(users);
    }

    @GetMapping
    public ResponseEntity<User> getUserData(@CookieValue("user_id") String userId) {
        User user = userService.getUserById(Long.valueOf(userId));
        if (user != null) {
            return ResponseEntity.ok(user);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

