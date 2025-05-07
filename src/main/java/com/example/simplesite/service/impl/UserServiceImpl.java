package com.example.simplesite.service.impl;

import com.example.simplesite.model.User;
import com.example.simplesite.repository.UserRepository;
import com.example.simplesite.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
//allArgsConstructor создает конструктор, который принимает аргументы для всех полей класса
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Override
    public void registerUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void findUser(String email) {
        userRepository.findByEmail(email);
    }

    @Override
    public void deleteUser(String email) {
        userRepository.deleteByEmail(email);
    }
}
