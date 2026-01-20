package dcc.authservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormateurSignUpResponse {
    private String userId;          // Keycloak user ID
    private String email;
    private String username;
    private String firstName;
    private String lastName;
    private String message;
}