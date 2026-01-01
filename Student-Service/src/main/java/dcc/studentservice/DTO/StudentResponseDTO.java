package dcc.studentservice.DTO;

import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentResponseDTO {
    private UUID id;

    private UUID keycloakUserId;

    private String firstName;

    private String lastName;

    private String email;

    private LocalDate birthDate;

    private String phone;

    private Boolean enabled;

    private LocalDate createdAt;

    private LocalDate updatedAt;
}
