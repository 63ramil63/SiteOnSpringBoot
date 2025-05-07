package com.example.simplesite.repository;


import com.example.simplesite.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

//Указываем у JpaRep класс объекта и тип его первичного ключа
public interface UserRepository extends JpaRepository<User, String> {
    void findByEmail(String email);
    void deleteByEmail(String email);
}
