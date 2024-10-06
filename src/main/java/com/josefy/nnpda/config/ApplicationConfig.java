package com.josefy.nnpda.config;

import lombok.Data;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collection;
import java.util.List;

@Configuration
public class ApplicationConfig {
    // TODO: REMOVE AND IMPLEMENT USER
    @Data
    class DetailsStub implements UserDetails {
        @Override
        public String getUsername() {
            return "admin";
        }

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            return List.of(new SimpleGrantedAuthority("ADMIN"));
        }

        @Override
        public String getPassword() {
            return "admin";
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

    @Bean
    public UserDetailsService userDetailsService() {
        var details = new DetailsStub();
        return username -> details;
    }
}
