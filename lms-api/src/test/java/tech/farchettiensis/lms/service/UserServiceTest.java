package tech.farchettiensis.lms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.farchettiensis.lms.model.User;
import tech.farchettiensis.lms.model.enums.UserRole;
import tech.farchettiensis.lms.repository.UserRepository;
import tech.farchettiensis.lms.service.impl.UserServiceImpl;

import java.util.Optional;

public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @Test
    void testRegisterUser_Success() {
        User user = new User("Jamil", "Mascarenhas", "jamil.mascarenhas@mascarenhasadv.com.br", "trupeAvela", UserRole.TEACHER);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.registerUser(user);

        verify(passwordEncoder).encode("trupeAvela");
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertEquals("jamil.mascarenhas@mascarenhasadv.com.br", registeredUser.getEmail());
        assertEquals(UserRole.TEACHER, registeredUser.getUserRole());
    }

    @Test
    void testRegisterUser_EmailAlreadyExists() {
        User user = new User("Jamil", "Mascarenhas", "jamil.mascarenhas@mascarenhasadv.com.br", "trupeAvela", UserRole.TEACHER);
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(user));
        assertEquals("Email already in use", exception.getMessage());
    }
}
