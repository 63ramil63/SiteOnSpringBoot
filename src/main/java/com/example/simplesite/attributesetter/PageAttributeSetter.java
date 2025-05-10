package com.example.simplesite.attributesetter;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public interface PageAttributeSetter {
    boolean isAuth(Authentication authentication);
    boolean isAdmin(Authentication authentication);
    void setAttribute(Model model, Authentication authentication);
}
