package dcc.authservice.service;

import dcc.authservice.Client.StudentClient;
import dcc.authservice.DTO.*;
import dcc.authservice.shared.CustomResponseException;
import dcc.authservice.shared.GlobalResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.UUID;


@Service
@Slf4j
public class AuthService {


    private  KeycloakService keycloakService;
    private  StudentClient studentClient;

    public AuthService(KeycloakService keycloakService, StudentClient studentClient) {
        this.keycloakService = keycloakService;
        this.studentClient = studentClient;
    }

    @Transactional
    public StudentSignUpResponse signUpStudent(StudentSignUpRequest request) {
        String keycloakUserId = null;

        try {
            // 1. Vérifier si l'email existe dans Keycloak
            if (keycloakService.emailExists(request.getEmail())) {
                throw CustomResponseException.Conflict("Email already exists");
            }

            // 2. Créer l'utilisateur dans Keycloak
            keycloakUserId = keycloakService.createUser(request);
            log.info("User created in Keycloak: {}", keycloakUserId);

            // 3. Préparer le DTO pour le Student Service
            StudentRequestDTO studentRequestDTO = StudentRequestDTO.builder()
                    .keycloakUserId(UUID.fromString(keycloakUserId))
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .email(request.getEmail())
                    .birthDate(request.getBirthDate())
                    .phone(request.getPhone())
                    .enabled(false)
                    .build();

            // 4. Créer le student via Feign Client
            GlobalResponse<StudentResponseDTO> response = studentClient.createStudent(studentRequestDTO);
            StudentResponseDTO studentResponse = response.getData();

            log.info("Student created in Student Service: {}", studentResponse.getId());

            // 5. Retourner la réponse de succès
            return StudentSignUpResponse.builder()
                    .id(studentResponse.getId().toString())
                    .keycloakUserId(keycloakUserId)
                    .email(studentResponse.getEmail())
                    .firstName(studentResponse.getFirstName())
                    .lastName(studentResponse.getLastName())
                    .message("Account created successfully. Please check your email to verify your account.")
                    .emailVerificationRequired(true)
                    .build();

        } catch (CustomResponseException e) {
            // En cas d'erreur, supprimer l'utilisateur de Keycloak si créé
            if (keycloakUserId != null) {
                log.error("Rollback: Deleting user from Keycloak due to error");
                keycloakService.deleteUser(keycloakUserId);
            }
            throw e;
        } catch (Exception e) {
            // En cas d'erreur inattendue
            if (keycloakUserId != null) {
                log.error("Rollback: Deleting user from Keycloak due to unexpected error");
                keycloakService.deleteUser(keycloakUserId);
            }
            log.error("Unexpected error during student sign up", e);
            throw CustomResponseException.InternalError("Sign up failed: " + e.getMessage());
        }
    }

    public LoginResponse login(LoginRequest request) {
        try {
            log.info("Login attempt for user: {}", request.getEmail());

            LoginResponse response = keycloakService.login(
                    request.getEmail(),
                    request.getPassword()
            );

            log.info("Login successful for user: {}", request.getEmail());
            return response;

        } catch (CustomResponseException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error during login", e);
            throw CustomResponseException.InternalError("Login failed: " + e.getMessage());
        }
    }

    // signUpFormateur

    @Transactional
    public FormateurSignUpResponse signUpFormateur(FormateurSignUpRequest request) {
        String keycloakUserId = null;

        try {
            // 1. Check if email exists
            if (keycloakService.emailExists(request.getEmail())) {
                throw CustomResponseException.Conflict("Email already exists");
            }

            // 2. Create user in Keycloak with ROLE_FORMATEUR
            keycloakUserId = keycloakService.createFormateur(request);
            log.info("Formateur created in Keycloak: {}", keycloakUserId);

            // 3. Return response
            return FormateurSignUpResponse.builder()
                    .userId(keycloakUserId)
                    .email(request.getEmail())
                    .username(request.getUsername())
                    .firstName(request.getFirstName())
                    .lastName(request.getLastName())
                    .message("Formateur account created successfully")
                    .build();

        } catch (CustomResponseException e) {
            // Rollback: delete from Keycloak if error
            if (keycloakUserId != null) {
                log.error("Rollback: Deleting formateur from Keycloak");
                keycloakService.deleteUser(keycloakUserId);
            }
            throw e;
        } catch (Exception e) {
            if (keycloakUserId != null) {
                log.error("Rollback: Deleting formateur from Keycloak");
                keycloakService.deleteUser(keycloakUserId);
            }
            log.error("Unexpected error during formateur sign up", e);
            throw CustomResponseException.InternalError("Sign up failed: " + e.getMessage());
        }
    }




}
