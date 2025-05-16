package com.example.simplesite.service.main.impl;

import com.example.simplesite.enums.Role;
import com.example.simplesite.model.main.User;
import com.example.simplesite.repository.main.UserRepository;
import com.example.simplesite.service.main.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
//allArgsConstructor создает конструктор, который принимает аргументы для всех полей класса
@AllArgsConstructor
public class UserServiceImpl implements UserService{

    private final UserRepository userRepository;
    private final PasswordEncoder encoder;

    @Override
    public boolean registerUser(User user) {
        if (findUser(user.getEmail()) == null) {
            user.setRole(Role.USER);
            user.setPassword(encoder.encode(user.getPassword()));
            user.setCache(1);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    @Override
    public User findUser(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void updateUserCache(String email, int newCache) {
        User user = userRepository.findByEmail(email);
        if (user != null) {
            user.setCache(newCache);
            userRepository.save(user);
        }
    }

    @Override
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }
}
