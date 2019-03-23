package com.example.paper.service;

import com.example.paper.dao.UserRepository;
import com.example.paper.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ManageUser {
    @Autowired
    private UserRepository userRepository;
    private Logger logger= LoggerFactory.getLogger(this.getClass());


    @Cacheable(value = "user",key = "#user.account",unless = "#result==null")
    public User AddUser(User user){
        if (userRepository.findByAccount(user.getAccount())==null) {
            return userRepository.save(user);
        }

        else{
            return null;
        }
    }
    @CachePut(value = "user",key = "#user.account")
    public User UpdateUser(User user){
        return userRepository.save(user);
    }
    @Cacheable(value = "user",key = "#account",unless = "#result==null")
    public User FindOne(String  account){
        logger.info("从数据库中找到"+account);
        return userRepository.findByAccount(account);
    }
}
