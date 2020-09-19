package com.raphau.springboot.stockExchange.service.ints;

import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.dto.UserUpdDTO;
import com.raphau.springboot.stockExchange.entity.User;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface UserService {

    List<User> findAll();
    User findById(int theId);
    void deleteById(int theId);
    void save(User theUser);
    Map<String, Object> getUserDetails();
    Optional<User> findByUsername(String username);
    TestDetailsDTO updateUser(UserUpdDTO userUpdDTO);

}
