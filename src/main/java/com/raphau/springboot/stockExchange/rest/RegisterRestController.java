package com.raphau.springboot.stockExchange.rest;

import com.raphau.springboot.stockExchange.dto.TestDetailsDTO;
import com.raphau.springboot.stockExchange.dto.UserDTO;
import com.raphau.springboot.stockExchange.entity.User;
import com.raphau.springboot.stockExchange.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterRestController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public TestDetailsDTO register(@RequestBody UserDTO userDTO){
        long timeApp = System.currentTimeMillis();
        TestDetailsDTO testDetailsDTO = new TestDetailsDTO();
        userDTO.setId(0);
        User user = new User(userDTO);
        long timeBase = System.currentTimeMillis();
        userService.save(user);
        testDetailsDTO.setDatabaseTime(System.currentTimeMillis() - timeBase);
        testDetailsDTO.setApplicationTime(System.currentTimeMillis() - timeApp);
        return testDetailsDTO;
    }

}
