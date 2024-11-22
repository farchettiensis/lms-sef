package tech.farchettiensis.lms.dto;

import tech.farchettiensis.lms.model.enums.UserRole;

import java.time.LocalDateTime;

public record UserResponseDTO(
        Long id,
        String email,
        String firstName,
        String lastName,
        UserRole role,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
