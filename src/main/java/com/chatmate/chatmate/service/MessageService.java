package com.chatmate.chatmate.service;

import com.chatmate.chatmate.entity.Channel;
import com.chatmate.chatmate.entity.Message;
import com.chatmate.chatmate.entity.User;
import com.chatmate.chatmate.repository.ChannelRepository;
import com.chatmate.chatmate.repository.MessageRepository;
import com.chatmate.chatmate.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MessageService {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;


    public Message sendMessageToChannel(Long userId, Long channelId, String content) {
        User sender = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with ID: " + userId));

        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));

        Message message = new Message();
        message.setSender(sender);
        message.setChannel(channel);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getMessagesByChannel(Long channelId) {
        Channel channel = channelRepository.findById(channelId)
                .orElseThrow(() -> new RuntimeException("Channel not found with ID: " + channelId));
        return messageRepository.findByChannelOrderByTimestampAsc(channel);
    }

    public Message sendDirectMessage(Long senderId, Long receiverId, String content) {
        User sender = userRepository.findById(senderId)
                .orElseThrow(() -> new RuntimeException("Sender not found with ID: " + senderId));

        User receiver = userRepository.findById(receiverId)
                .orElseThrow(() -> new RuntimeException("Receiver not found with ID: " + receiverId));

        Message message = new Message();
        message.setSender(sender);
        message.setReceiver(receiver);
        message.setContent(content);
        message.setTimestamp(LocalDateTime.now());

        return messageRepository.save(message);
    }

    public List<Message> getDirectMessages(Long userId1, Long userId2) {
        return messageRepository.findDirectMessages(userId1, userId2);
    }

}

