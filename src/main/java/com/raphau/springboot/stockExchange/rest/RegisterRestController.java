package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public void register(@RequestBody User user){

        user.setId(0);
        userService.save(user);

    }

}
