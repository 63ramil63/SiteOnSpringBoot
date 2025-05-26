package com.example.simplesite.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = "params")
public class ParamsConfig {
    private int productsPerPage;
}
