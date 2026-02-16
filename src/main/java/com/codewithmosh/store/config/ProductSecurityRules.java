package com.codewithmosh.store.config;

import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.stereotype.Component;

import com.codewithmosh.store.entities.Role;

@Component
public class ProductSecurityRules implements SecurityRules {
    @Override
    public void configure(
        AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry registry) {
            registry.requestMatchers(HttpMethod.GET, "/products/**").permitAll()
                .requestMatchers(HttpMethod.POST,"/products/**").hasRole(Role.ADMIN.toString()) // admin access
                .requestMatchers(HttpMethod.PUT,"/products/**").hasRole(Role.ADMIN.toString()) // admin access
                .requestMatchers(HttpMethod.DELETE,"/products/**").hasRole(Role.ADMIN.toString()); // admin access
    }
    
}
