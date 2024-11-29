package tech.farchettiensis.lms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.farchettiensis.lms.dto.UserRegistrationDTO;
import tech.farchettiensis.lms.dto.UserResponseDTO;
import tech.farchettiensis.lms.exception.DuplicateEmailException;
import tech.farchettiensis.lms.model.User;
import tech.farchettiensis.lms.model.enums.UserRole;
import tech.farchettiensis.lms.repository.UserRepository;
import tech.farchettiensis.lms.service.UserService;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public UserResponseDTO registerUser(UserRegistrationDTO userRegistrationDTO) {
        if (userRepository.findByEmail(userRegistrationDTO.email()).isPresent()) {
            throw new DuplicateEmailException("Email already in use");
        }

        User user = new User(
                userRegistrationDTO.firstName(),
                userRegistrationDTO.lastName(),
                userRegistrationDTO.email(),
                passwordEncoder.encode(userRegistrationDTO.password()),
                userRegistrationDTO.role() != null ? userRegistrationDTO.role() : UserRole.STUDENT
        );

        User savedUser = userRepository.save(user);

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getEmail(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getUserRole(),
                savedUser.getCreatedAt(),
                savedUser.getUpdatedAt()
        );
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
