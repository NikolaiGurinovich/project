package com.example.project.service;

import com.example.project.model.Followers;
import com.example.project.model.User;
import com.example.project.model.dto.UpdateUserDto;
import com.example.project.repository.FollowersRepository;
import com.example.project.repository.UserRepository;
import com.example.project.security.enums.Roles;
import com.example.project.security.model.SecurityUser;
import com.example.project.security.repository.UserSecurityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final UserSecurityRepository userSecurityRepository;
    private final FollowersRepository followersRepository;

    @Autowired
    public UserService(UserRepository userRepository, UserSecurityRepository userSecurityRepository, FollowersRepository followersRepository) {
        this.userRepository = userRepository;
        this.userSecurityRepository = userSecurityRepository;
        this.followersRepository = followersRepository;
    }

    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(Long id) {
        return userRepository.findById(id);
    }

    public Boolean deleteUserById(Long id) {
        Optional<User> userCheck = getUserById(id);
        if (userCheck.isEmpty()) {
            return false;
        }
        userRepository.deleteById(id);
        return userRepository.findById(id).isEmpty();
    }

    public Boolean createUser(User user) {
        User newUser = new User();
        newUser.setUserName(user.getUserName());
        newUser.setUserAge(user.getUserAge());
        newUser.setUserWeight(user.getUserWeight());
        newUser.setGender(user.getGender());
        newUser.setCreated(Timestamp.valueOf(LocalDateTime.now()));
        newUser.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        User createdUser = userRepository.save(newUser);
        return getUserById(createdUser.getId()).isPresent();
    }

    public Boolean updateUser(User user) {
        Optional<User> userFromDBOptional = userRepository.findById(user.getId());
        if (userFromDBOptional.isPresent()) {
            User userFromDB = userFromDBOptional.get();
            if (user.getUserName() != null) {
                userFromDB.setUserName(user.getUserName());
            }
            if (user.getUserAge() != null) {
                userFromDB.setUserAge(user.getUserAge());
            }
            userFromDB.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
            User updatedUser = userRepository.saveAndFlush(userFromDB);
            return userFromDB.equals(updatedUser);
        }
        return false;
    }

    public Optional<User> getInfoAboutCurrentUser(String userName) {
        Optional<SecurityUser> userSecurity = userSecurityRepository.findByUserLogin(userName);
        if (userSecurity.isEmpty()) {
            return Optional.empty();
        }
        return userRepository.findById(userSecurity.get().getUserId());
    }

    public Boolean subscribe(Principal principal, Long id) {
        Optional<SecurityUser> userSecurity = userSecurityRepository.findByUserLogin(principal.getName());
        if (userSecurity.isEmpty()) {
            return false;
        }
        Followers follow = new Followers();
        if (followersRepository.existsAllByUserIdAndSubUserId(id, userSecurity.get().getUserId())) {
            return false;
        }
        follow.setUserId(id);
        follow.setSubUserId(userSecurity.get().getUserId());
        Followers savedFollow = followersRepository.save(follow);
        return followersRepository.existsById(savedFollow.getId());
    }

    public Boolean changeUserToAdmin(Long id) {
        Optional<User> userCheck = getUserById(id);
        if (userCheck.isEmpty()) {
            return false;
        }
        SecurityUser securityUser = userSecurityRepository.findByUserId(id).get();
        securityUser.setRole(Roles.ADMIN);
        SecurityUser updatedUser = userSecurityRepository.saveAndFlush(securityUser);
        return !updatedUser.equals(userCheck.get());
    }

    public Boolean unsubscribe(Principal principal, Long id) {
        Optional<SecurityUser> userSecurity = userSecurityRepository.findByUserLogin(principal.getName());
        if (userSecurity.isEmpty()) {
            return false;
        }
        if (!followersRepository.existsAllByUserIdAndSubUserId(id, userSecurity.get().getUserId())) {
            return false;
        }
        Optional<Followers> followers = followersRepository.findAllByUserIdAndSubUserId(id, userSecurity.get().getUserId());
        if (followers.isEmpty()) {
            return false;
        }
        followersRepository.delete(followers.get());
        return !followersRepository.existsById(followers.get().getId());
    }

    public Boolean updateCurrentUser(UpdateUserDto updateUserDto, Principal principal) {
        Optional<User> user = getInfoAboutCurrentUser(principal.getName());
        if (user.isEmpty()) {
            return false;
        }
        User userToUpdate = user.get();
        if (updateUserDto.getUserName() != null) {
            userToUpdate.setUserName(updateUserDto.getUserName());
        }
        if (updateUserDto.getUserAge() != null) {
            userToUpdate.setUserAge(updateUserDto.getUserAge());
        }
        if (updateUserDto.getUserWeight() != null) {
            userToUpdate.setUserWeight(updateUserDto.getUserWeight());
        }
        if (updateUserDto.getGender() != null) {
            userToUpdate.setGender(updateUserDto.getGender());
        }
        userToUpdate.setUpdated(Timestamp.valueOf(LocalDateTime.now()));
        User updatedUser = userRepository.save(userToUpdate);
        return userToUpdate.equals(updatedUser);
    }
}