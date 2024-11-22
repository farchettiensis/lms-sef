package tech.farchettiensis.lms.service;

import tech.farchettiensis.lms.dto.UserRegistrationDTO;
import tech.farchettiensis.lms.dto.UserResponseDTO;
import tech.farchettiensis.lms.model.User;

public interface UserService {
    UserResponseDTO registerUser(UserRegistrationDTO registrationDTO);

    User findByEmail(String email);
}

