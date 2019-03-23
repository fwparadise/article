package com.example.paper.dao;

import com.example.paper.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User,String> {
    User findByAccount(String account);
}
