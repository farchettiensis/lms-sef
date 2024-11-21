package tech.farchettiensis.lms.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
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
    public User registerUser(User user) {
        if (userRepository.findByEmail(user.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        if (user.getUserRole() == null) {
            user.setUserRole(UserRole.STUDENT);
        }

        return userRepository.save(user);
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }
}
