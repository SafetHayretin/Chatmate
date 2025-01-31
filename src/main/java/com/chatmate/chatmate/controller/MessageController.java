package com.chatmate.chatmate.controller;

import com.chatmate.chatmate.dto.MessageDTO;
import com.chatmate.chatmate.dto.MessageRequest;
import com.chatmate.chatmate.entity.Message;
import com.chatmate.chatmate.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping("/channel/{channelId}")
    public ResponseEntity<List<MessageDTO>> getMessagesByChannel(@PathVariable Long channelId) {
        List<MessageDTO> messages = messageService.getMessagesByChannel(channelId)
                .stream()
                .map(msg -> new MessageDTO(
                        msg.getId(),
                        msg.getSender().getUsername(),
                        msg.getContent(),
                        msg.getChannel().getName(),
                        msg.getTimestamp()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(messages);
    }

    @GetMapping("/direct")
    public ResponseEntity<List<MessageDTO>> getDirectMessages(
            @CookieValue("user_id") String userId,
            @RequestParam Long userId2) {
        List<MessageDTO> messages = messageService.getDirectMessages(Long.valueOf(userId), userId2)
                .stream()
                .map(msg -> new MessageDTO(
                        msg.getId(),
                        msg.getSender().getUsername(),
                        msg.getContent(),
                        msg.getReceiver(),
                        msg.getTimestamp()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(messages);
    }

    @PostMapping("/send")
    public ResponseEntity<MessageDTO> sendMessageToChannel(
            @CookieValue(value = "user_id", required = false) String userId,
            @RequestBody MessageRequest request) {

        if (userId == null) {
            return ResponseEntity.status(401).build();
        }

        Message message = messageService.sendMessageToChannel(Long.valueOf(userId), request.getChannelId(), request.getContent());

        MessageDTO messageDTO = new MessageDTO(
                message.getId(),
                message.getSender().getUsername(),
                message.getContent(),
                message.getChannel().getName(),
                message.getTimestamp()
        );

        return ResponseEntity.ok(messageDTO);
    }

    @PostMapping("/send-direct")
    public ResponseEntity<MessageDTO> sendDirectMessage(
            @CookieValue("user_id") String userId,
            @RequestBody MessageRequest request) {
        Message message = messageService.sendDirectMessage(Long.valueOf(userId), request.getReceiverId(), request.getContent());

        MessageDTO messageDTO = new MessageDTO(
                message.getId(),
                message.getSender().getUsername(),
                message.getContent(),
                message.getReceiver(),
                message.getTimestamp()
        );

        return ResponseEntity.ok(messageDTO);
    }
}

