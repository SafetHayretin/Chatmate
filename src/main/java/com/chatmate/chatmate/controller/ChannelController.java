package com.chatmate.chatmate.controller;

import com.chatmate.chatmate.dto.AddUserRequest;
import com.chatmate.chatmate.dto.ChannelDto;
import com.chatmate.chatmate.dto.UserDTO;
import com.chatmate.chatmate.entity.Channel;
import com.chatmate.chatmate.entity.ChannelRole;
import com.chatmate.chatmate.entity.RoleName;
import com.chatmate.chatmate.service.ChannelRoleService;
import com.chatmate.chatmate.service.ChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/channels")
public class ChannelController {

    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelRoleService channelRoleService;

    @GetMapping
    public ResponseEntity<List<ChannelDto>> getAllChannels() {
        List<ChannelDto> channels = channelService.getAllChannels();

        return ResponseEntity.ok(channels);
    }

    @GetMapping("/{channelId}/users")
    public ResponseEntity<List<UserDTO>> getChannelUsers(@PathVariable Long channelId) {
        List<UserDTO> users = channelService.getUsersInChannel(channelId);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/user")
    public ResponseEntity<List<ChannelDto>> getChannelsByUser(@CookieValue("user_id") String userId) {
        List<ChannelDto> channels = channelService.getChannelsByUser(Long.valueOf(userId));
        return ResponseEntity.ok(channels);
    }

    @GetMapping("/not-added-users")
    public ResponseEntity<List<UserDTO>> getUsersNotInChannel(
            @CookieValue("user_id") String userId,
            @RequestParam Long channelId) {
        List<UserDTO> usersNotInChannel = channelService.getUsersNotInChannel(Long.valueOf(userId), channelId);

        return ResponseEntity.ok(usersNotInChannel);
    }

    @PostMapping("/add-user")
    public ResponseEntity<ChannelRole> addUserToChannel(
            @CookieValue("user_id") String userId,
            @RequestBody AddUserRequest request) {
        ChannelRole channelRole = channelRoleService.addRoleToUserInChannel(
                Long.valueOf(request.getUserId()),
                request.getChannelId(),
                request.getRoleName());

        return ResponseEntity.ok(channelRole);
    }

    @PostMapping("/create")
    public ResponseEntity<ChannelDto> createChannel(
            @CookieValue("user_id") String userId,
            @RequestParam String channelName) {

        ChannelDto channel = channelService.createChannel(Long.valueOf(userId), channelName);
        return ResponseEntity.ok(channel);
    }

    @PutMapping("/{channelId}/rename")
    public ResponseEntity<ChannelDto> renameChannel(
            @CookieValue("user_id") String userId,
            @PathVariable Long channelId,
            @RequestBody Map<String, String> requestBody) {

        String newName = requestBody.get("newName");
        ChannelDto updatedChannel = channelService.renameChannel(Long.valueOf(userId), channelId, newName);

        return ResponseEntity.ok(updatedChannel);
    }

    @DeleteMapping("/{channelId}")
    public ResponseEntity<String> deleteChannel(@PathVariable Long channelId, @CookieValue("user_id") String userId) {
        try {
            channelService.deleteChannel(channelId, Long.valueOf(userId));
            return ResponseEntity.ok().body("Каналът беше изтрит успешно.");
        } catch (RuntimeException e) {
            return ResponseEntity.status(403).body(e.getMessage());
        }
    }
}

