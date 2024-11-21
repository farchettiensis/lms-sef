package tech.farchettiensis.lms.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import tech.farchettiensis.lms.model.User;
import tech.farchettiensis.lms.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    @ResponseStatus(HttpStatus.CREATED)
    public User registerUser(@Valid @RequestBody User user) {
        return userService.registerUser(user);
    }
}
