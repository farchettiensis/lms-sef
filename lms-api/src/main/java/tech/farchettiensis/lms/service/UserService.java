package tech.farchettiensis.lms.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import tech.farchettiensis.lms.dto.UserRegistrationDTO;
import tech.farchettiensis.lms.dto.UserResponseDTO;

import java.util.Optional;

public interface UserService {

    UserResponseDTO registerUser(UserRegistrationDTO registrationDTO);

    Optional<UserResponseDTO> updateUser(Long id, UserRegistrationDTO registrationDTO);

    Optional<UserResponseDTO> findByEmail(String email);

    Optional<UserResponseDTO> findById(Long id);

    Page<UserResponseDTO> findAll(Pageable pageable);

    void deleteUser(Long id);
}

