package tech.farchettiensis.lms.dto;

import tech.farchettiensis.lms.model.enums.UserRole;

public record UserDTO(String email, String password, String firstName, String lastName, UserRole role) {
}
