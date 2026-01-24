package dcc.formationservice.Controller;




import dcc.formationservice.DTO.CreateSessionRequest;
import dcc.formationservice.DTO.SessionResponse;
import dcc.formationservice.DTO.UpdateSessionRequest;
import dcc.formationservice.Service.SessionService;
import dcc.formationservice.shared.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/api/sessions")
public class SessionController {
    private SessionService sessionService;
    public SessionController(SessionService sessionService) {
        this.sessionService = sessionService;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_FORMATTER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<SessionResponse>> createSession(
            @Valid @RequestPart("session") CreateSessionRequest request,
            @RequestPart(value = "content", required = false) MultipartFile contentFile) {

        SessionResponse response = sessionService.createSession(request, contentFile);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(response));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_FORMATTER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<SessionResponse>> updateSession(
            @PathVariable UUID id,
            @Valid @RequestPart("session") UpdateSessionRequest request,
            @RequestPart(value = "content", required = false) MultipartFile contentFile) {

        SessionResponse response = sessionService.updateSession(id, request, contentFile);
        return ResponseEntity.ok(new GlobalResponse<>(response));
    }

    @GetMapping("/section/{sectionId}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_FORMATTER', 'ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<List<SessionResponse>>> getSessionsBySection(
            @PathVariable UUID sectionId) {

        List<SessionResponse> response = sessionService.getSessionsBySection(sectionId);
        return ResponseEntity.ok(new GlobalResponse<>(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_FORMATTER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<String>> deleteSession(@PathVariable UUID id) {
        sessionService.deleteSession(id);
        return ResponseEntity.ok(new GlobalResponse<>("Session successfully deleted"));
    }
}
