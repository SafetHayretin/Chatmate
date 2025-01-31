package com.chatmate.chatmate.dto;

import com.chatmate.chatmate.entity.User;

import java.time.LocalDateTime;

public class MessageDTO {
    private Long id;
    private String sender;
    private String content;
    private String channelName;
    private String receiver;
    private LocalDateTime timestamp;

    public MessageDTO(Long id, String sender, String content, String channelName, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.channelName = channelName;
        this.timestamp = timestamp;
    }
    public MessageDTO(Long id, String sender, String content, User receiver, LocalDateTime timestamp) {
        this.id = id;
        this.sender = sender;
        this.content = content;
        this.receiver = receiver.getUsername();
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

    public void setId(Long id) {
        this.id = id;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
}
