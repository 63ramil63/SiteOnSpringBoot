package com.example.simplesite.service;

import com.example.simplesite.model.User;
import jakarta.transaction.Transactional;

public interface UserService {
    //следит за тем, чтобы действия при работе с базой данных выполнялись как надо: или все, или ничего
    @Transactional
    boolean registerUser(User user);
    User findUser(String email);
    void deleteUser(String email);
}
