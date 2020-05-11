package com.raphau.springboot.stockExchange.service;

import com.raphau.springboot.stockExchange.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    User findById(int theId);
    void deleteById(int theId);
    void save(User theUser);
    Optional<User> findByUsername(String username);
}
