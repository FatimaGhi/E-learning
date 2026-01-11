package dcc.authservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentSignUpResponse {

    private String id;
    private String keycloakUserId;
    private String email;
    private String firstName;
    private String lastName;
    private String message;
    private boolean emailVerificationRequired;
}
