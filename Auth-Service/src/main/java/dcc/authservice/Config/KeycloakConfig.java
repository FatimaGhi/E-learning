package dcc.authservice.Config;

import lombok.Getter;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;


@Getter
@Configuration
public class KeycloakConfig {
//    @Value("${keycloak.auth-server-url}")
//    private String authServerUrl;
//
//    @Value("${keycloak.realm}")
//    private String realm;
//
//    @Value("${keycloak.admin-username}")
//    private String adminUsername;
//
//    @Value("${keycloak.admin-password}")
//    private String adminPassword;

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.client-id}")
    private String clientId;

    @Value("${keycloak.client-secret}")
    private String clientSecret;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    @Value("${keycloak.admin-client-id:admin-cli}")
    private String adminClientId;

    @Bean
    public Keycloak keycloak() {
        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm("master") // Admin realm
                .username(adminUsername)
                .password(adminPassword)
                .clientId("admin-cli")
                .build();
    }


}
