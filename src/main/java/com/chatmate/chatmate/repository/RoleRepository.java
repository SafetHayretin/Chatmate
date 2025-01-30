package com.chatmate.chatmate.repository;

import com.chatmate.chatmate.entity.Role;
import com.chatmate.chatmate.entity.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}

