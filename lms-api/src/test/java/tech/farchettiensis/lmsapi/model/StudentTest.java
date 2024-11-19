package tech.farchettiensis.lmsapi.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import jakarta.validation.ConstraintViolation;
import org.junit.jupiter.api.BeforeAll;

import java.util.Set;

public class StudentTest {

    private static Validator validator;

    @BeforeAll
    public static void setUpValidator() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void testStudentCreation() {
        String firstName = "Jamil";
        String lastName = "Platão";
        String email = "jamil.platao@jamil.org";

        Student student = new Student(firstName, lastName, email);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);
        assertTrue(violations.isEmpty(), "There should be no validation errors");

        assertNotNull(student);
        assertEquals(firstName, student.getFirstName());
        assertEquals(lastName, student.getLastName());
        assertEquals(email, student.getEmail());
    }

    @Test
    void testInvalidEmail() {
        String firstName = "Jamil";
        String lastName = "Jhoses";
        String email = "invalid-email";

        Student student = new Student(firstName, lastName, email);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty(), "Validation errors expected");
        assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        assertEquals("Invalid email address", violation.getMessage());
        assertEquals("email", violation.getPropertyPath().toString());
    }

    @Test
    void testNullFirstName() {
        String lastName = "Amarelo";
        String email = "jamil.amarelo@jamil.com";

        Student student = new Student(null, lastName, email);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        assertEquals("First name is required", violation.getMessage());
        assertEquals("firstName", violation.getPropertyPath().toString());
    }

    @Test
    void testEmptyLastName() {
        String firstName = "Vermelho";
        String email = "jamil.vermelho@jamil.com";

        Student student = new Student(firstName, "", email);

        Set<ConstraintViolation<Student>> violations = validator.validate(student);

        assertFalse(violations.isEmpty());
        assertEquals(1, violations.size());

        ConstraintViolation<Student> violation = violations.iterator().next();
        assertEquals("Last name is required", violation.getMessage());
        assertEquals("lastName", violation.getPropertyPath().toString());
    }

}

