package com.ecommerce.store.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfig() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:4200", "https://store4.vercel.app", "https://store6.vercel.app", "https://store-testing4.vercel.app")
                        .allowedMethods(HttpMethod.GET.name(), HttpMethod.PUT.name(), HttpMethod.POST.name(), HttpMethod.DELETE.name());
            }
        };
    }
}
