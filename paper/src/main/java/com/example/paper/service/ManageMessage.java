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
    private final MessageRepository messageRepository;

    @Autowired
    public ManageMessage(MessageRepository messageRepository) {
        this.messageRepository = messageRepository;
    }


    public List<Message> getMessages(String to) {
        return messageRepository.findByReceiver(to);
    }
    public void deleteMessages(String to){
        messageRepository.deleteByReceiver(to);
    }
    public void add(Message message){
        messageRepository.save(message);
    }
}
