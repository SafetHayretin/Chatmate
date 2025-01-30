package com.chatmate.chatmate.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomUserDetails implements UserDetails {
    private final User user;

    public CustomUserDetails(User user) {
        this.user = user;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> list = new ArrayList<GrantedAuthority>();
        List<String> roles = user.getRoles();
        for (String role : roles) {
            list.add(new SimpleGrantedAuthority(role));
        }
        return list;
    }

    public String getUserId() {
        return user.getId() + "";
    }

    @Override
    public String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {
        return user.getEmail(); // Use email as the login username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true; // Update if you add account expiration logic
    }

    @Override
    public boolean isAccountNonLocked() {
        return true; // Update if you add account locking logic
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true; // Update if you add credential expiration logic
    }

    @Override
    public boolean isEnabled() {
        return true; // Update if you add an "enabled" field
    }
}
