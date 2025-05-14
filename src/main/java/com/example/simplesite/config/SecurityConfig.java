package com.example.simplesite.config;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private UserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        //доступ к страницам и ресурсам проекта
                        .requestMatchers("/register", "/registerForm", "/login").anonymous()
                        .requestMatchers("/market", "/").permitAll()
                        .requestMatchers("/styles/**", "/images/**").permitAll()
                        .requestMatchers("/order", "/orders", "/deleteOrder", "/buyOrder", "/fillBalance", "/balance").hasAnyAuthority("ADMIN", "USER")
                        .requestMatchers("/adminPanel", "/addProduct").hasAuthority("ADMIN"))
                .formLogin(form -> form
                        //настройка формы для захода в аккаунт
                        .loginPage("/login")
                        .failureForwardUrl("/registerForm")
                        //параметры указанные на странице html по атрибуту name
                        .usernameParameter("email")
                        .passwordParameter("password")
                        .defaultSuccessUrl("/market", true))
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/market")
                        .permitAll());

        return http.build();
    }
}