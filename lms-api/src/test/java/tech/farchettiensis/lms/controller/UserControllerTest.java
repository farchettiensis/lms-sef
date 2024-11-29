package tech.farchettiensis.lms.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import tech.farchettiensis.lms.config.JwtProperties;
import tech.farchettiensis.lms.dto.UserRegistrationDTO;
import tech.farchettiensis.lms.dto.UserResponseDTO;
import tech.farchettiensis.lms.exception.DuplicateEmailException;
import tech.farchettiensis.lms.model.enums.UserRole;
import tech.farchettiensis.lms.service.CustomUserDetailsService;
import tech.farchettiensis.lms.service.TokenService;
import tech.farchettiensis.lms.service.UserService;

import java.time.LocalDateTime;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = UserController.class)
@AutoConfigureMockMvc(addFilters = false)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private TokenService tokenService;

    @MockBean
    private JwtProperties jwtProperties;

    @MockBean
    private CustomUserDetailsService customUserDetailsService;

    @BeforeEach
    public void setUp() {
        when(jwtProperties.getSecret()).thenReturn("Bzg20pFBAvwxVskcB6GjNYdVvmXB2vMT");
        when(jwtProperties.getExpirationMs()).thenReturn(3600000L);

        when(tokenService.validateToken(anyString())).thenReturn(true);
    }

    @Test
    public void testRegisterUser_Success() throws Exception {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "jamil.mascarenhas@mascarenhas.adv.com.br",
                "jamaIsTheLaw",
                "Jamil",
                "Mascarenhas",
                UserRole.TEACHER
        );

        UserResponseDTO userResponseDTO = new UserResponseDTO(
                1L,
                "jamil.mascarenhas@mascarenhas.adv.com.br",
                "Jamil",
                "Mascarenhas",
                UserRole.TEACHER,
                LocalDateTime.now(),
                LocalDateTime.now()
        );

        when(userService.registerUser(any(UserRegistrationDTO.class))).thenReturn(userResponseDTO);

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRegistrationDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("User successfully registered"))
                .andExpect(jsonPath("$.data.email").value("jamil.mascarenhas@mascarenhas.adv.com.br"))
                .andExpect(jsonPath("$.data.firstName").value("Jamil"))
                .andExpect(jsonPath("$.data.lastName").value("Mascarenhas"))
                .andExpect(jsonPath("$.data.role").value("TEACHER"));

        verify(userService).registerUser(any(UserRegistrationDTO.class));
    }

    @Test
    public void testRegisterUser_EmailAlreadyExists() throws Exception {
        UserRegistrationDTO userRegistrationDTO = new UserRegistrationDTO(
                "existing.email@example.com",
                "password123",
                "John",
                "Doe",
                UserRole.STUDENT
        );

        when(userService.registerUser(any(UserRegistrationDTO.class)))
                .thenThrow(new DuplicateEmailException("Email already in use"));

        mockMvc.perform(post("/api/users/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(userRegistrationDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Email already in use"))
                .andExpect(jsonPath("$.data").doesNotExist());

        verify(userService).registerUser(any(UserRegistrationDTO.class));
    }
}
