package com.example.simplesite.service.main;

import com.example.simplesite.model.main.User;
import jakarta.transaction.Transactional;

public interface UserService {
    //следит за тем, чтобы действия при работе с базой данных выполнялись как надо: или все, или ничего
    @Transactional
    boolean registerUser(User user);
    User findUser(String email);
    @Transactional
    void updateUserCache(String email, int newCache);
    void deleteUser(String email);
}
