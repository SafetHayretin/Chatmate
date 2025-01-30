package com.chatmate.chatmate.dto;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private String sender;
    private String content;
    private String channelName;
    private LocalDateTime timestamp;

    public MessageDTO(Long id, String sender, String content, String channelName, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.channelName = channelName;
        this.timestamp = timestamp;
    }

    public Long getId() {
        return id;
    }

    public String getSender() {
        return sender;
    }

    public String getContent() {
        return content;
    }

    public String getChannelName() {
        return channelName;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }
}
