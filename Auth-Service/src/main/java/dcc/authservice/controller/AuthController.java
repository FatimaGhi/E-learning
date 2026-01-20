package dcc.authservice.controller;

import dcc.authservice.DTO.*;
import dcc.authservice.service.AuthService;
import dcc.authservice.shared.GlobalResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@Slf4j
public class AuthController {

     private AuthService authService;

     public AuthController(AuthService authService) {
         this.authService = authService;
     }
    @PostMapping("/signup/student")
    public ResponseEntity<GlobalResponse<StudentSignUpResponse>> signUpStudent(
            @Valid @RequestBody StudentSignUpRequest request) {

        log.info("Received sign up request for email: {}", request.getEmail());
        StudentSignUpResponse response = authService.signUpStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(response));
    }
    @PostMapping("/login")
    public ResponseEntity<GlobalResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        log.info("Login request received for: {}", request.getEmail());
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(new GlobalResponse<>(response));
    }


    @PostMapping("/formattor/register")
    public ResponseEntity<GlobalResponse<FormateurSignUpResponse>> registerFormateur(
            @Valid @RequestBody FormateurSignUpRequest request,
            @RequestHeader("Authorization") String authHeader) {

        log.info(" Received formateur registration request for: {}", request.getEmail());
        log.info(" Authorization header present: {}", authHeader != null);

        FormateurSignUpResponse response = authService.signUpFormateur(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(response));
    }
}
