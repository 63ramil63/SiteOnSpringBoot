package com.example.simplesite.service;

import com.example.simplesite.model.User;
import org.springframework.stereotype.Service;

@Service
public interface UserService {
    void registerUser(User user);
    void findUser(String email);
    void deleteUser(String email);
}
