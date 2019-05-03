package com.example.paper.service;

import com.example.paper.dao.UserRepository;
import com.example.paper.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional
public class ManageUser {
    private final UserRepository userRepository;

    @Autowired
    public ManageUser(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    // private Logger logger= LoggerFactory.getLogger(this.getClass());


    @Cacheable(value = "user", key = "#user.getAccount", unless = "#result==null")
    public User AddUser(User user) {
        return userRepository.save(user);
    }

    @CachePut(value = "user", key = "#user.account", unless = "#result==null")
    public User UpdateUser(User user) {
        return userRepository.save(user);
    }

    @Cacheable(value = "user", key = "#account", unless = "#result==null")
    public User FindOne(String account) {
        return userRepository.findByAccount(account);
    }
}
