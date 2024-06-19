package com.example.project.controller;

import com.example.project.model.User;
import com.example.project.model.dto.UpdateUserDto;
import com.example.project.service.UserService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<User>> getAllUsers() {
        log.info("start method getAllUsers in UserController");
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<User> getUserById(@PathVariable("id") Long id) {
        log.info("start method getUserById in UserController");
        Optional<User> user = userService.getUserById(id);
        if (user.isPresent()) {
            return new ResponseEntity<>(user.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> createUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(userService.createUser(user) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @PutMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> updateUser(@RequestBody @Valid User user, BindingResult bindingResult) {
        log.info("start method updateUser in UserController(Admin)");
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        return new ResponseEntity<>(userService.updateUser(user) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> deleteUserById(@PathVariable("id") Long id) {
        log.info("start method deleteUserById in UserController");
        return new ResponseEntity<>(userService.deleteUserById(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @GetMapping("/info")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
    public ResponseEntity<User> getInfoAboutCurrentUser(Principal principal) {
        Optional<User> result = userService.getInfoAboutCurrentUser(principal.getName());
        if (result.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result.get(), HttpStatus.OK);
    }

    @PostMapping("/subscribe/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
    public ResponseEntity<HttpStatus> subscribe(@PathVariable("id") Long id, Principal principal) {
        log.info("start method subscribe in UserController");
        Optional<User> subscriber = userService.getInfoAboutCurrentUser(principal.getName());
        if (subscriber.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<User> userToSubscribe = userService.getUserById(id);
        if (userToSubscribe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (subscriber.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userService.subscribe(principal, id) ? HttpStatus.CREATED : HttpStatus.CONFLICT);
    }

    @DeleteMapping("/subscribe/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
    public ResponseEntity<HttpStatus> unsubscribe(@PathVariable("id") Long id, Principal principal) {
        log.info("start method unsubscribe in UserController");
        Optional<User> subscriber = userService.getInfoAboutCurrentUser(principal.getName());
        if (subscriber.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<User> userToUnsubscribe = userService.getUserById(id);
        if (userToUnsubscribe.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        if (subscriber.get().getId().equals(id)) {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }
        return new ResponseEntity<>(userService.unsubscribe(principal, id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyRole('ADMIN','USER','GROUP_ADMIN')")
    public ResponseEntity<HttpStatus> updateCurrentUser(@RequestBody @Valid UpdateUserDto updateUserDto,
                                                        BindingResult bindingResult, Principal principal) {
        log.info("start method updateUser in UserController");
        if (bindingResult.hasErrors()) {
            log.error(bindingResult.getFieldError().getDefaultMessage());
        }
        Optional<User> userToUpdate = userService.getInfoAboutCurrentUser(principal.getName());
        if (userToUpdate.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.updateCurrentUser(updateUserDto, principal)
                ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }

    @PutMapping("/admin/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<HttpStatus> changeUserToAdmin(@PathVariable("id") Long id, Principal principal) {
        log.info("start method changeUserToAdmin in UserController");
        Optional<User> admin = userService.getInfoAboutCurrentUser(principal.getName());
        if (admin.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        Optional<User> userToChange = userService.getUserById(id);
        if (userToChange.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(userService.changeUserToAdmin(id) ? HttpStatus.NO_CONTENT : HttpStatus.CONFLICT);
    }
}
