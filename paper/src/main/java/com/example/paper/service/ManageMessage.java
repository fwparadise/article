package com.example.paper.service;

import com.example.paper.dao.MessageRepository;
import com.example.paper.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional
public class ManageMessage {
    @Autowired
    private MessageRepository messageRepository;


    public List<Message> getMessages(String to) {
        return messageRepository.findByReceiver(to);
    }
    public List<Message> deleteMessages(String to){
        return messageRepository.deleteByReceiver(to);
    }
    public void add(Message message){
        messageRepository.save(message);
    }
}
