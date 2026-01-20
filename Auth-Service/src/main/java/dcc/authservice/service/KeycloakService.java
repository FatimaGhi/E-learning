package dcc.authservice.service;

import dcc.authservice.Config.KeycloakConfig;
import dcc.authservice.DTO.FormateurSignUpRequest;
import dcc.authservice.DTO.LoginResponse;
import dcc.authservice.DTO.StudentSignUpRequest;
import dcc.authservice.DTO.UserInfo;
import dcc.authservice.shared.CustomResponseException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.stereotype.Service;


import java.util.*;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KeycloakService {
    private final Keycloak keycloak;
    private final KeycloakConfig keycloakConfig;

    public KeycloakService(KeycloakConfig keycloakConfig, Keycloak keycloak) {
        this.keycloakConfig = keycloakConfig;
        this.keycloak = keycloak;
    }

    public String createUser(StudentSignUpRequest request) {
        try {
            RealmResource realmResource = keycloak.realm(keycloakConfig.getRealm());
            UsersResource usersResource = realmResource.users();

            // Use full class name to avoid confusion
            org.keycloak.representations.idm.UserRepresentation user =
                    new org.keycloak.representations.idm.UserRepresentation();

            user.setUsername(request.getEmail());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEnabled(true); // HAD VERSION 21.1.1 tkhdem haka
            user.setEmailVerified(false);

            Response response = usersResource.create(user);

            if (response.getStatus() == 409) {
                throw CustomResponseException.Conflict("Email already exists in Keycloak");
            }

            if (response.getStatus() != 201) {
                String errorMsg = response.readEntity(String.class);
                log.error("Failed to create user in Keycloak: {}", errorMsg);
                throw CustomResponseException.InternalError("Failed to create user in Keycloak");
            }

            String locationHeader = response.getHeaderString("Location");
            String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

            setPassword(userId, request.getPassword());
            assignRole(userId, "ROLE_STUDENT");
            sendVerificationEmail(userId);

            log.info("User created successfully in Keycloak with ID: {}", userId);
            return userId;

        } catch (Exception e) {
            log.error("Error creating user in Keycloak", e);
            throw CustomResponseException.InternalError("Error creating user in Keycloak: " + e.getMessage());
        }
    }

    private void setPassword(String userId, String password) {
        try {
            org.keycloak.representations.idm.CredentialRepresentation credential =
                    new org.keycloak.representations.idm.CredentialRepresentation();

            credential.setType("password");
            credential.setValue(password);
            credential.setTemporary(false);

            RealmResource realmResource = keycloak.realm(keycloakConfig.getRealm());
            realmResource.users().get(userId).resetPassword(credential);
        } catch (Exception e) {
            log.error("Error setting password", e);
            throw CustomResponseException.InternalError("Error setting password");
        }
    }

    private void sendVerificationEmail(String userId) {
        try {
            RealmResource realmResource = keycloak.realm(keycloakConfig.getRealm());
            realmResource.users().get(userId).sendVerifyEmail();
            log.info("Verification email sent to user: {}", userId);
        } catch (Exception e) {
            log.warn("Could not send verification email: {}", e.getMessage());
        }
    }

    private void assignRole(String userId, String roleName) {
        try {
            RealmResource realmResource = keycloak.realm(keycloakConfig.getRealm());
            var role = realmResource.roles().get(roleName).toRepresentation();
            realmResource.users().get(userId).roles().realmLevel().add(List.of(role));
            log.info("Role {} assigned to user {}", roleName, userId);
        } catch (Exception e) {
            log.error("Error assigning role", e);
        }
    }
    public boolean emailExists(String email) {
        try {
            RealmResource realmResource = keycloak.realm(keycloakConfig.getRealm());
            UsersResource usersResource = realmResource.users();

            List<org.keycloak.representations.idm.UserRepresentation> users =
                    usersResource.searchByEmail(email, true);

            return !users.isEmpty();
        } catch (Exception e) {
            log.error(" This Email is already in Keycloak", e);
            return false;
        }
    }
    public void deleteUser(String userId) {
        try {
            RealmResource realmResource = keycloak.realm(keycloakConfig.getRealm());
            UserResource userResource = realmResource.users().get(userId);

            if (userResource != null) {
                userResource.remove();
                log.info("this user : {} is delete from keycloak ", userId);
            }
        } catch (Exception e) {
            log.error("Error deleting user from Keycloak", e);
            throw CustomResponseException.InternalError("Error deleting user from Keycloak: " + e.getMessage());
        }
    }


    public LoginResponse login(String email, String password) {
        try {

            Keycloak userKeycloak = KeycloakBuilder.builder()
                    .serverUrl(keycloakConfig.getAuthServerUrl())
                    .realm(keycloakConfig.getRealm())
                    .clientId(keycloakConfig.getClientId())
                    .clientSecret(keycloakConfig.getClientSecret())
                    .username(email)
                    .password(password)
                    .build();

            AccessTokenResponse tokenResponse = userKeycloak.tokenManager().getAccessToken();


            RealmResource realmResource = keycloak.realm(keycloakConfig.getRealm());
            UsersResource usersResource = realmResource.users();

            List<org.keycloak.representations.idm.UserRepresentation> users =
                    usersResource.searchByEmail(email, true);

            if (users.isEmpty()) {
                throw CustomResponseException.Unauthorized("Invalid credentials");
            }

            org.keycloak.representations.idm.UserRepresentation user = users.get(0);


            if (!user.isEnabled()) {
                throw CustomResponseException.Forbidden("Account is disabled. Please verify your email.");
            }

            UserResource userResource = usersResource.get(user.getId());
            List<RoleRepresentation> roles = userResource.roles().realmLevel().listAll();
            List<String> roleNames = roles.stream()
                    .map(RoleRepresentation::getName)
                    .collect(Collectors.toList());


            UserInfo userInfo = UserInfo.builder()
                    .id(UUID.fromString(user.getId()))
                    .firstName(user.getFirstName())
                    .lastName(user.getLastName())
                    .email(user.getEmail())
                    .roles(roleNames)
                    .build();

            return LoginResponse.builder()
                    .accessToken(tokenResponse.getToken())
                    .refreshToken(tokenResponse.getRefreshToken())
                    .tokenType("Bearer")
                    .expiresIn(tokenResponse.getExpiresIn())
                    .user(userInfo)
                    .build();

        } catch (Exception e) {
            log.error("Login failed for user: {}", email, e);
            throw CustomResponseException.Unauthorized("Invalid email or password");
        }
    }
//add formattor
    public String createFormateur(FormateurSignUpRequest request) {
        try {
            RealmResource realmResource = keycloak.realm(keycloakConfig.getRealm());
            UsersResource usersResource = realmResource.users();

            // Create user representation
            org.keycloak.representations.idm.UserRepresentation user =
                    new org.keycloak.representations.idm.UserRepresentation();

            user.setUsername(request.getUsername());
            user.setEmail(request.getEmail());
            user.setFirstName(request.getFirstName());
            user.setLastName(request.getLastName());
            user.setEnabled(true);
            user.setEmailVerified(true); // Formateurs are pre-verified by admin

            Response response = usersResource.create(user);

            if (response.getStatus() == 409) {
                throw CustomResponseException.Conflict("Email or username already exists in Keycloak");
            }

            if (response.getStatus() != 201) {
                String errorMsg = response.readEntity(String.class);
                log.error("Failed to create formateur in Keycloak: {}", errorMsg);
                throw CustomResponseException.InternalError("Failed to create formateur in Keycloak");
            }

            String locationHeader = response.getHeaderString("Location");
            String userId = locationHeader.substring(locationHeader.lastIndexOf('/') + 1);

            // Set password
            setPassword(userId, request.getPassword());

            // Assign ROLE_FORMATEUR
            assignRole(userId, "ROLE_FORMATTER");

            log.info("Formateur created successfully in Keycloak with ID: {}", userId);
            return userId;

        } catch (Exception e) {
            log.error("Error creating formateur in Keycloak", e);
            throw CustomResponseException.InternalError("Error creating formateur: " + e.getMessage());
        }
    }

}