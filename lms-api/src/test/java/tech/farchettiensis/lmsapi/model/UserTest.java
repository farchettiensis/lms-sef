package tech.farchettiensis.lmsapi.model;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
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
        String email = "dana.white@example.com";
        String password = "securepassword";
        UserRole role = null; // TODO: role that doesn't exist

        User user = new User(firstName, lastName, email, password, role);

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        assertFalse(violations.isEmpty(), "Validation errors expected");
        assertTrue(violations.stream().anyMatch(v -> v.getPropertyPath()
                .toString()
                .equals("userRole")
                && v.getMessage().equals("User role is required")));
    }
}
