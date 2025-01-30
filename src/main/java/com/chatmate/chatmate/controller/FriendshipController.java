package com.chatmate.chatmate.controller;

import com.chatmate.chatmate.dto.FriendRequestDTO;
import com.chatmate.chatmate.dto.UserDTO;
import com.chatmate.chatmate.entity.Friendship;
import com.chatmate.chatmate.entity.User;
import com.chatmate.chatmate.service.FriendshipService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/friendships")
public class FriendshipController {
    private final FriendshipService friendshipService;

    public FriendshipController(FriendshipService friendshipService) {
        this.friendshipService = friendshipService;
    }

    @GetMapping("/friends")
    public ResponseEntity<List<UserDTO>> getFriends(@CookieValue("user_id") String userId) {
        List<UserDTO> friends = friendshipService.getFriends(Long.valueOf(userId));
        return ResponseEntity.ok(friends);
    }

    @PostMapping("/add")
    public ResponseEntity<Friendship> addFriend(
            @CookieValue(value = "user_id", required = false) String userId,
            @RequestBody FriendRequestDTO request) {

        if (userId == null) {
            return ResponseEntity.status(401).build(); // Unauthorized
        }

        Friendship friendship = friendshipService.addFriend(Long.valueOf(userId), request.getFriendId());
        return ResponseEntity.ok(friendship);
    }
}

