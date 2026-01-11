package dcc.authservice.DTO;


import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentSignUpRequest {

    @NotBlank(message = "First name is required")
    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    private String firstName;

    @NotBlank(message = "Last name is required")
    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    private String lastName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    private String email;

    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Password must contain at least one uppercase, one lowercase and one digit")
    private String password;

    @Past(message = "Birth date must be in the past")
    private LocalDate birthDate;

    @Pattern(
            regexp = "^(\\+212|0)[5-7][0-9]{8}$",
            message = "Phone number must be a valid Moroccan number"
    )
    private String phone;
}
