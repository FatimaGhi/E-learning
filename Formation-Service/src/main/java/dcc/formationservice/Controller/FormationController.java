package dcc.formationservice.Controller;


import dcc.formationservice.DTO.CreateFormationRequest;
import dcc.formationservice.DTO.FormationListResponse;
import dcc.formationservice.DTO.FormationResponse;
import dcc.formationservice.DTO.UpdateFormationRequest;
import dcc.formationservice.Service.FormationService;
import dcc.formationservice.shared.GlobalResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RestController
@RequestMapping("/api/formations")
public class FormationController {


    private FormationService formationService;

    public FormationController(FormationService formationService){
        this.formationService = formationService ;
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_FORMATTER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<FormationResponse>> createFormation(
            @Valid @RequestPart("formation") CreateFormationRequest request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        FormationResponse response = formationService.createFormation(request, imageFile);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new GlobalResponse<>(response));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ROLE_FORMATTER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<FormationResponse>> updateFormation(
            @PathVariable UUID id,
            @Valid @RequestPart("formation") UpdateFormationRequest request,
            @RequestPart(value = "image", required = false) MultipartFile imageFile) {

        FormationResponse response = formationService.updateFormation(id, request, imageFile);
        return ResponseEntity.ok(new GlobalResponse<>(response));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_FORMATTER', 'ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<FormationResponse>> getFormationById(@PathVariable UUID id) {
        FormationResponse response = formationService.getFormationById(id);
        return ResponseEntity.ok(new GlobalResponse<>(response));
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_FORMATTER', 'ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<Page<FormationListResponse>>> getAllFormations(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String direction) {

        Sort.Direction sortDirection = Sort.Direction.fromString(direction);
        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(sortDirection, sortBy));

        Page<FormationListResponse> response = formationService.getAllFormations(pageRequest);
        return ResponseEntity.ok(new GlobalResponse<>(response));
    }

    @GetMapping("/category/{categoryId}")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_FORMATTER', 'ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<Page<FormationListResponse>>> getFormationsByCategory(
            @PathVariable Long categoryId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
        Page<FormationListResponse> response = formationService.getFormationsByCategory(categoryId, pageRequest);
        return ResponseEntity.ok(new GlobalResponse<>(response));
    }

    @GetMapping("/search")
    @PreAuthorize("hasAnyRole('ROLE_STUDENT', 'ROLE_FORMATTER', 'ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<Page<FormationListResponse>>> searchFormations(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        PageRequest pageRequest = PageRequest.of(page, size);
        Page<FormationListResponse> response = formationService.searchFormations(keyword, pageRequest);
        return ResponseEntity.ok(new GlobalResponse<>(response));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ROLE_FORMATTER') or hasRole('ROLE_ADMIN')")
    public ResponseEntity<GlobalResponse<String>> deleteFormation(@PathVariable UUID id) {
        formationService.deleteFormation(id);
        return ResponseEntity.ok(new GlobalResponse<>("Training successfully deleted"));
    }
}
