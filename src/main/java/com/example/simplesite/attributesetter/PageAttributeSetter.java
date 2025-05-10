package com.example.simplesite.attributesetter;

import org.springframework.security.core.Authentication;
import org.springframework.ui.Model;

public interface PageAttributeSetter {
    void setHeaderAttribute(Model model, Authentication authentication);
    void setBodyAttribute(Model model);
}
