package com.chatmate.chatmate.repository;

import com.chatmate.chatmate.entity.Friendship;
import com.chatmate.chatmate.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {
    boolean existsByUserAndFriend(User user, User friend);
    List<Friendship> findAllByUser(User user);
    @Query("SELECT f.friend FROM Friendship f WHERE f.user.id = :userId")
    List<User> findFriendsByUserId(Long userId);
}

