package tech.farchettiensis.lms.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum UserRole {
    STUDENT("STUDENT"),
    TEACHER("TEACHER"),
    ADMIN("ADMIN");

    private final String role;
}
