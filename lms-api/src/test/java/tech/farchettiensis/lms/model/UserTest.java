package tech.farchettiensis.lms.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import tech.farchettiensis.lms.model.enums.UserRole;
import tech.farchettiensis.lms.repository.UserRepository;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
public class UserTest {
    private static ValidatorFactory factory;
    private static Validator validator;

    private final UserRepository userRepository;

    @Autowired
    public UserTest(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @BeforeAll
    public static void setUpValidator() {
        factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @AfterAll
    public static void closeValidatorFactory() {
        factory.close();
    }

    @Test
    void testUserCreation() {
        String firstName = "Jamil";
        String lastName = "Mascarenhas";
        String email = "jamil.mascarenhas@mascarenhasadv.com.br";
        String password = "M4sc4r3nh@s";
        UserRole role = UserRole.TEACHER;

        User user = new User(firstName, lastName, email, password, role);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertTrue(violations.isEmpty(), "No validation errors expected");

        assertNotNull(user);
        assertEquals(firstName, user.getFirstName());
        assertEquals(lastName, user.getLastName());
        assertEquals(email, user.getEmail());
        assertEquals(password, user.getPassword());
        assertEquals(role, user.getUserRole());
    }

    @Test
    void testInvalidEmail() {
        String firstName = "Jamil";
        String lastName = "Mascarenhas";
        String email = "jamil.mascarenhas";
        String password = "M4sc4r3nh@s";
        UserRole role = UserRole.TEACHER;

        User user = new User(firstName, lastName, email, password, role);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation errors expected");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString()
                        .equals("email") &&
                        v.getMessage().equals("Invalid email address")));
    }

    @Test
    void testDuplicateEmail() {
        String email = "jamil.mascarenhas@mascarenhasadv.com";
        User user1 = new User("Jamil", "Mascarenhas", email, "doutorjamilAdv", UserRole.TEACHER);
        User user2 = new User("Jama", "Jhoses", email, "jamilAdvocacias", UserRole.TEACHER);

        userRepository.save(user1);

        Exception exception = assertThrows(DataIntegrityViolationException.class, () -> {
            userRepository.save(user2);
        });

        String expectedMessage = "could not execute statement";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }

    @Test
    void testNullPassword() {
        String firstName = "Charlie";
        String lastName = "Brown";
        String email = "charlie.brown@example.com";
        String password = null;
        UserRole role = UserRole.ADMIN;

        User user = new User(firstName, lastName, email, password, role);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation errors expected");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString()
                        .equals("password")
                        && v.getMessage().equals("Password is required")));
    }

    @Test
    void testShortPassword() {
        String password = "jama";

        User user = new User("Jamil", "José", "jamil.jose@jhoses.hotdog.com", password, UserRole.STUDENT);

        Set<ConstraintViolation<User>> violations = validator.validate(user);

        assertFalse(violations.isEmpty(), "Validation errors expected");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString()
                        .equals("password")
                        && v.getMessage().equals("Password must be at least 6 characters long")));
    }

    @Test
    void testPasswordEncryption() {
    }

    @Test
    void testInvalidRole() {
        String firstName = "Dana";
        String lastName = "White";
        String email = "dana.white@ufc.com";
        String password = "conorMcJones";
        UserRole role = null;

        User user = new User(firstName, lastName, email, password, role);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation errors expected");
        assertTrue(violations.stream()
                .anyMatch(v -> v.getPropertyPath()
                        .toString()
                        .equals("userRole")
                        && v.getMessage().equals("User role is required")));
    }

    @Test
    void testInvalidRoleValue() {
        String json = "{"
                + "\"firstName\":\"Dana\","
                + "\"lastName\":\"White\","
                + "\"email\":\"dana.white@ufc.com\","
                + "\"password\":\"jonJones\","
                + "\"userRole\":\"Swagger da rizzle\""
                + "}";

        ObjectMapper objectMapper = new ObjectMapper();

        Exception exception = assertThrows(InvalidFormatException.class, () -> {
            User user = objectMapper.readValue(json, User.class);
        });

        String expectedMessage = "Cannot deserialize value of type";
        String actualMessage = exception.getMessage();

        assertTrue(actualMessage.contains(expectedMessage));
    }
}
