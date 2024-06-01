package com.example.project.security.service;

import com.example.project.security.model.SecurityUser;
import com.example.project.security.repository.UserSecurityRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
public class CustomUserDetailService implements UserDetailsService {

    private final UserSecurityRepository userSecurityRepository;

    @Autowired
    public CustomUserDetailService(UserSecurityRepository userSecurityRepository) {
        this.userSecurityRepository = userSecurityRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<SecurityUser> securityInfoOptional = userSecurityRepository.findByUserLogin(username);
        if (securityInfoOptional.isEmpty()){
            throw new UsernameNotFoundException("Username not found: " + username);
        }
        SecurityUser security = securityInfoOptional.get();
        return User.builder()
                .username(security.getUserLogin())
                .password(security.getPassword())
                .roles(security.getRole().toString())
                .build();
    }
}
