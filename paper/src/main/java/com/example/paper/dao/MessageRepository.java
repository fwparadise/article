package com.example.paper.dao;

import com.example.paper.entity.Message;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MessageRepository extends JpaRepository<Message,String> {
     List<Message> findByReceiver(String to);
     List<Message> deleteByReceiver(String to);
}
