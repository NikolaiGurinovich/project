package com.example.project.security.service;

import com.example.project.exeptions.UserAlreadyExists;
import com.example.project.model.User;
import com.example.project.repository.UserRepository;
import com.example.project.security.enums.Roles;
import com.example.project.security.model.SecurityUser;
import com.example.project.security.model.dto.AuthRequestDto;
import com.example.project.security.model.dto.RegistrationDto;
import com.example.project.security.repository.UserSecurityRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class UserSecurityService {

    private final UserSecurityRepository userSecurityRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    @Autowired
    public UserSecurityService(UserSecurityRepository userSecurityRepository, UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtils jwtUtils) {
        this.userSecurityRepository = userSecurityRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @Transactional(rollbackFor = Exception.class)
    public void registration(RegistrationDto registrationDto){
        Optional<SecurityUser> security = userSecurityRepository.findByUserLogin(registrationDto.getLogin());
        if (security.isPresent()){
            throw new UserAlreadyExists(registrationDto.getLogin());
        }
        User user = new User();
        user.setUserName(registrationDto.getUserName());
        user.setUserAge(registrationDto.getUserAge());
        user.setGender(registrationDto.getGender());
        user.setUserWeight(registrationDto.getUserWeight());
        user.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        user.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        User savedUser = userRepository.save(user);

        SecurityUser securityUser = new SecurityUser();
        securityUser.setUserLogin(registrationDto.getLogin());
        securityUser.setPassword(passwordEncoder.encode(registrationDto.getPassword()));
        securityUser.setRole(Roles.USER);
        securityUser.setUserId(savedUser.getId());
        userSecurityRepository.save(securityUser);
    }

    public Optional<String> generateToken(AuthRequestDto authRequestDto){
        Optional<SecurityUser> security = userSecurityRepository.findByUserLogin(authRequestDto.getLogin());
        if (security.isPresent()
                && passwordEncoder.matches(authRequestDto.getPassword(), security.get().getPassword())){
            return Optional.of(jwtUtils.generateJwtToken(authRequestDto.getLogin()));
        }
        return Optional.empty();
    }
}
