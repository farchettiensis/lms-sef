package tech.farchettiensis.lms.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import tech.farchettiensis.lms.dto.ResponseDTO;
import tech.farchettiensis.lms.dto.UserRegistrationDTO;
import tech.farchettiensis.lms.dto.UserResponseDTO;
import tech.farchettiensis.lms.service.UserService;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<ResponseDTO<UserResponseDTO>> registerUser(
            @Valid @RequestBody UserRegistrationDTO registrationDTO) {
        UserResponseDTO userResponse = userService.registerUser(registrationDTO);
        ResponseDTO<UserResponseDTO> response = new ResponseDTO<>(
                "User successfully registered",
                LocalDateTime.now(),
                userResponse
        );
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
}
