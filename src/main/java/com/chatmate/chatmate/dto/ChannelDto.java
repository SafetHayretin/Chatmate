package com.chatmate.chatmate.dto;

import com.chatmate.chatmate.entity.Channel;

public class ChannelDto {
    private Long id;

    private String name;

    private String role;

    public ChannelDto(Channel channel) {
        this.id = channel.getId();
        this.name = channel.getName();
    }
    public ChannelDto(Channel channel, String role) {
        this.id = channel.getId();
        this.name = channel.getName();
        this.role = role;
    }

    public ChannelDto(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
