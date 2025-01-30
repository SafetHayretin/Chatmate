package com.chatmate.chatmate.dto;

import com.chatmate.chatmate.entity.Channel;

public class ChannelDto {
    public ChannelDto(Channel channel) {
        this.id = channel.getId();
        this.name = channel.getName();
    }

    private Long id;

    private String name;

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
}
