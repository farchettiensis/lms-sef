package tech.farchettiensis.lms.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import tech.farchettiensis.lms.dto.UserRegistrationDTO;
import tech.farchettiensis.lms.dto.UserResponseDTO;
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
    private AutoCloseable closeable;

    @BeforeEach
    void setUp() {
        closeable = MockitoAnnotations.openMocks(this);
        userService = new UserServiceImpl(userRepository, passwordEncoder);
    }

    @AfterEach
    void tearDown() throws Exception {
        closeable.close();
    }

    @Test
    void testRegisterUser_Success() {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "jamil.mascarenhas@mascarenhasadv.com.br",
                "trupeAvela",
                "Jamil",
                " Mascarenhas",
                UserRole.TEACHER
        );

        User user = new User(
                "Jamil",
                "Mascarenhas",
                "jamil.mascarenhas@mascarenhasadv.com.br",
                "encodedPassword",
                UserRole.TEACHER
        );

        when(userRepository.findByEmail(userRegistrationDTO.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(userRegistrationDTO.password())).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenReturn(user);

        UserResponseDTO responseDTO = userService.registerUser(userRegistrationDTO);

        verify(passwordEncoder).encode("trupeAvela");

        assertEquals("jamil.mascarenhas@mascarenhasadv.com.br", responseDTO.email());
        assertEquals("Jamil", responseDTO.firstName());
        assertEquals("Mascarenhas", responseDTO.lastName());
        assertEquals(UserRole.TEACHER, responseDTO.role());
    }


    @Test
    void testRegisterUser_EmailAlreadyExists() {
        User existingUser = new User(
                "Jamil",
                "Mascarenhas",
                "jamil.mascarenhas@mascarenhasadv.com.br",
                "encodedPassword",
                UserRole.TEACHER
        );

        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "Jamil",
                "Mascarenhas",
                "jamil.mascarenhas@mascarenhasadv.com.br",
                "trupeAvela",
                UserRole.TEACHER
        );

        when(userRepository.findByEmail(userRegistrationDTO.email())).thenReturn(Optional.of(existingUser));

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                userService.registerUser(userRegistrationDTO));

        assertEquals("Email already in use", exception.getMessage());
    }

}
