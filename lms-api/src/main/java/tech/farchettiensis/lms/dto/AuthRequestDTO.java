package tech.farchettiensis.lms.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class AuthRequestDTO {

    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
