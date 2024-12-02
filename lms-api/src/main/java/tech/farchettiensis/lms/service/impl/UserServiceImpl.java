package tech.farchettiensis.lms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import tech.farchettiensis.lms.dto.UserRegistrationDTO;
import tech.farchettiensis.lms.dto.UserResponseDTO;
import tech.farchettiensis.lms.exception.DuplicateEmailException;
import tech.farchettiensis.lms.exception.ResourceNotFoundException;
import tech.farchettiensis.lms.model.User;
import tech.farchettiensis.lms.model.enums.UserRole;
import tech.farchettiensis.lms.repository.UserRepository;
import tech.farchettiensis.lms.service.UserService;

import java.util.Optional;

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
                this.passwordEncoder.encode(userRegistrationDTO.password()),
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
    public Optional<UserResponseDTO> updateUser(Long id, UserRegistrationDTO registrationDTO) {
        return userRepository.findById(id).map(user -> {
            if (!user.getEmail().equals(registrationDTO.email()) && userRepository.findByEmail(registrationDTO.email()).isPresent()) {
                throw new DuplicateEmailException("Email already in use");
            }

            user.setFirstName(registrationDTO.firstName());
            user.setLastName(registrationDTO.lastName());
            user.setEmail(registrationDTO.email());

            User updatedUser = userRepository.save(user);

            return new UserResponseDTO(
                    updatedUser.getId(),
                    updatedUser.getEmail(),
                    updatedUser.getFirstName(),
                    updatedUser.getLastName(),
                    updatedUser.getUserRole(),
                    updatedUser.getCreatedAt(),
                    updatedUser.getUpdatedAt()
            );
        });
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User with ID " + id + " not found"));
        userRepository.delete(user);
    }

    @Override
    public Optional<UserResponseDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUserRole(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                ));
    }

    @Override
    public Optional<UserResponseDTO> findById(Long id) {
        return userRepository.findById(id)
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUserRole(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                ));
    }

    @Override
    public Page<UserResponseDTO> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(user -> new UserResponseDTO(
                        user.getId(),
                        user.getEmail(),
                        user.getFirstName(),
                        user.getLastName(),
                        user.getUserRole(),
                        user.getCreatedAt(),
                        user.getUpdatedAt()
                ));
    }
}
