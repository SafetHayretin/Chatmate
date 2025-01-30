package com.chatmate.chatmate.controller;

import com.chatmate.chatmate.entity.ChannelRole;
import com.chatmate.chatmate.entity.RoleName;
import com.chatmate.chatmate.service.ChannelRoleService;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/channel-roles")
public class ChannelRoleController {
    private final ChannelRoleService channelRoleService;

    public ChannelRoleController(ChannelRoleService channelRoleService) {
        this.channelRoleService = channelRoleService;
    }

    @PostMapping("/assign")
    public ResponseEntity<ChannelRole> assignRole(
            @RequestParam Long userId,
            @RequestParam Long channelId,
            @RequestParam RoleName roleName) {
        ChannelRole channelRole = channelRoleService.addRoleToUserInChannel(userId, channelId, roleName);

        return ResponseEntity.ok(channelRole);
    }

    @DeleteMapping("/{channelId}/remove-guest")
    public ResponseEntity<Void> removeGuestFromChannel(
            @PathVariable Long channelId,
            @CookieValue("user_id") String ownerId,
            @RequestParam Long guestUserId) {

        channelRoleService.removeGuestFromChannel(Long.valueOf(ownerId), channelId, guestUserId);
        return ResponseEntity.noContent().build();
    }
}

