package com.chatmate.chatmate.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;

    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ChannelRole> channelRoles = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<ChannelRole> getChannelRoles() {
        return channelRoles;
    }

    @Transient
    public List<String> getRoles() {
        // Map roles from channelRoles (if they represent user roles globally)
        return channelRoles.stream()
                .map(channelRole -> channelRole.getRole().name())
                .distinct()
                .toList();
    }

    public void setChannelRoles(List<ChannelRole> channelRoles) {
        this.channelRoles = channelRoles;
    }
}

