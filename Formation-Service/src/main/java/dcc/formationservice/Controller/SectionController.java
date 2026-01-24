package dcc.formationservice.Controller;



import dcc.formationservice.DTO.CreateSectionRequest;
import dcc.formationservice.DTO.SectionResponse;
import dcc.formationservice.DTO.UpdateSectionRequest;
import dcc.formationservice.Service.SectionService;
import dcc.formationservice.shared.GlobalResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/sections")
public class SectionController {


    private SectionService sectionService;

    public SectionController(SectionService sectionService) {
        this.sectionService = sectionService;
    }

    @PostMapping
    @PreAuthorize("hasRole('ROLE_FORMATTER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<SectionResponse>> createSection(
            @Valid @RequestBody CreateSectionRequest request) {

        SectionResponse response = sectionService.createSection(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(response));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_FORMATTER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<SectionResponse>> updateSection(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateSectionRequest request) {

        SectionResponse response = sectionService.updateSection(id, request);
        return ResponseEntity.ok(new GlobalResponse<>(response));
    }

    @GetMapping("/formation/{formationId}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_FORMATTER', 'ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<List<SectionResponse>>> getSectionsByFormation(
            @PathVariable UUID formationId) {

        List<SectionResponse> response = sectionService.getSectionsByFormation(formationId);
        return ResponseEntity.ok(new GlobalResponse<>(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_FORMATTER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<String>> deleteSection(@PathVariable UUID id) {
        sectionService.deleteSection(id);
        return ResponseEntity.ok(new GlobalResponse<>("Section deleted successfully"));
    }
}
