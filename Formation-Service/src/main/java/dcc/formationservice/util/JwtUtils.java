package dcc.formationservice.util;


import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class JwtUtils {

    public Jwt getCurrentJwt() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof Jwt) {
            return (Jwt) authentication.getPrincipal();
        }
        return null;
    }

    public String getCurrentUsername() {
        Jwt jwt = getCurrentJwt();
        if (jwt != null) {
            return jwt.getClaim("preferred_username");
        }
        return null;
    }


    public String getCurrentUserEmail() {
        Jwt jwt = getCurrentJwt();
        if (jwt != null) {
            return jwt.getClaim("email");
        }
        return null;
    }

    public String getCurrentUserFullName() {
        Jwt jwt = getCurrentJwt();
        if (jwt != null) {
            String firstName = jwt.getClaim("given_name");
            String lastName = jwt.getClaim("family_name");
            if (firstName != null && lastName != null) {
                return firstName + " " + lastName;
            }
            return jwt.getClaim("name");
        }
        return null;
    }

    public List<String> getCurrentUserRoles() {
        Jwt jwt = getCurrentJwt();
        if (jwt != null) {
            Map<String, Object> realmAccess = jwt.getClaim("realm_access");
            if (realmAccess != null) {
                return (List<String>) realmAccess.get("roles");
            }
        }
        return List.of();
    }


    public boolean hasRole(String role) {
        return getCurrentUserRoles().contains(role);
    }


    public String getCurrentUserId() {
        Jwt jwt = getCurrentJwt();
        if (jwt != null) {
            return jwt.getSubject();
        }
        return null;
    }
}
