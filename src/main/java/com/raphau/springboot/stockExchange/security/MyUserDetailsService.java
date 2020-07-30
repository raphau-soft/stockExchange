package com.raphau.springboot.stockExchange.security;

import com.raphau.springboot.stockExchange.dao.UserRepository;
import com.raphau.springboot.stockExchange.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class MyUserDetailsService implements UserDetailsService {

    @Autowired
    UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(login);

        user.orElseThrow(() -> new UsernameNotFoundException("Not Found: " + login));

        return user.map(MyUserDetails::new).get();
    }
}
