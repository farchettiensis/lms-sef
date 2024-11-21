package tech.farchettiensis.lmsapi.model;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import jakarta.validation.*;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static ValidatorFactory factory;
    private static Validator validator;

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
        String email = "jamil.mascarenhas@mascarenhasadv.com";
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
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath()
                .toString()
                .equals("email") &&
                v.getMessage().equals("Invalid email address")));
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
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath()
                .toString()
                .equals("password")
                && v.getMessage().equals("Password is required")));
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
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath()
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
