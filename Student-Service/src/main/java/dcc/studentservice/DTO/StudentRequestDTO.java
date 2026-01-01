package dcc.studentservice.DTO;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentRequestDTO {
    @NotNull(message = "Keycloak user id is required")
    private UUID keycloakUserId;

    @NotBlank(message = "First name is required")
    private String firstName;

    @NotBlank(message = "Last name is required")
    private String lastName;

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Pattern(
            regexp = "^(\\+212|0)[5-7][0-9]{8}$",
            message = "Phone number must be a valid Moroccan number"
    )
    private String phone;
}
