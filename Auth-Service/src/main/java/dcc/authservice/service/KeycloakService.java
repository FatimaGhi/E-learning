package dcc.authservice.service;

import dcc.authservice.Config.KeycloakConfig;
import dcc.authservice.DTO.StudentSignUpRequest;
import dcc.authservice.shared.CustomResponseException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

}