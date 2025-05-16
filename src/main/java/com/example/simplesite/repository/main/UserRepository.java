package com.example.simplesite.repository.main;


import com.example.simplesite.model.main.User;
import org.springframework.data.jpa.repository.JpaRepository;

//Указываем у JpaRep класс объекта и тип его первичного ключа
public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    void deleteByEmail(String email);
}
