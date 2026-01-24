package dcc.formationservice.Service;


import dcc.formationservice.DTO.CreateFormationRequest;
import dcc.formationservice.DTO.FormationListResponse;
import dcc.formationservice.DTO.FormationResponse;
import dcc.formationservice.DTO.UpdateFormationRequest;
import dcc.formationservice.Entites.Category;
import dcc.formationservice.Entites.Formation;
import dcc.formationservice.Mapper.FormationMapper;
import dcc.formationservice.Repo.CategoryRepository;
import dcc.formationservice.Repo.FormationRepository;
import dcc.formationservice.shared.CustomResponseException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@Service
@Slf4j
public class FormationService {

    private  FormationRepository formationRepository;
    private  CategoryRepository categoryRepository;
    private  MinIOService minIOService;
    private  FormationMapper formationMapper;

    public FormationService(FormationRepository formationRepository, CategoryRepository categoryRepository, MinIOService minIOService, FormationMapper formationMapper){
        this.categoryRepository = categoryRepository;
        this.formationMapper = formationMapper;
        this.minIOService = minIOService;
        this.formationRepository = formationRepository;
    }

    @Transactional
    public FormationResponse createFormation(CreateFormationRequest request, MultipartFile imageFile) {
        // Valid category
        Category category = categoryRepository.findById(request.getCategoryId())
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Category not found"));

        // Validate the promo code if it exists
        if (request.getPromoCode() != null && formationRepository.existsByPromoCode(request.getPromoCode())) {
            throw CustomResponseException.Conflict("This promo code already exists");
        }

        // Upload the image to MinIO
        String imageUrl = null;
        if (imageFile != null && !imageFile.isEmpty()) {
            imageUrl = minIOService.uploadFile(imageFile, "formations/images");
        }

        //  Use the mapper to create the entity
        Formation formation = formationMapper.toEntity(request, category, imageUrl);
        formation = formationRepository.save(formation);

        log.info("Training successfully created: {}", formation.getId());

        // Use the mapper to create the response
        return formationMapper.toResponse(formation);
    }

    @Transactional
    public FormationResponse updateFormation(UUID id, UpdateFormationRequest request, MultipartFile imageFile) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Training not found"));

        // Retrieve the category if provided
        Category category = null;
        if (request.getCategoryId() != null) {
            category = categoryRepository.findById(request.getCategoryId())
                    .orElseThrow(() -> CustomResponseException.ResourceNotFound("Category not found"));
        }

        // Use the mapper to update
        formationMapper.updateEntity(formation, request, category);

        // Update the image if provided
        if (imageFile != null && !imageFile.isEmpty()) {
            // Delete the old image
            if (formation.getImageUrl() != null) {
                minIOService.deleteFile(formation.getImageUrl());
            }
            String newImageUrl = minIOService.uploadFile(imageFile, "formations/images");
            formation.setImageUrl(newImageUrl);
        }

        formation = formationRepository.save(formation);
        log.info("update training: {}", formation.getId());

        return formationMapper.toResponse(formation);
    }

    @Transactional(readOnly = true)
    public FormationResponse getFormationById(UUID id) {
        Formation formation = formationRepository.findByIdAndIsActiveTrue(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Training not found"));

        return formationMapper.toResponse(formation);
    }

    @Transactional(readOnly = true)
    public Page<FormationListResponse> getAllFormations(Pageable pageable) {
        return formationRepository.findByIsActiveTrue(pageable)
                .map(formationMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public Page<FormationListResponse> getFormationsByCategory(Long categoryId, Pageable pageable) {
        return formationRepository.findByCategoryIdAndIsActiveTrue(categoryId, pageable)
                .map(formationMapper::toListResponse);
    }

    @Transactional(readOnly = true)
    public Page<FormationListResponse> searchFormations(String keyword, Pageable pageable) {
        return formationRepository.searchFormations(keyword, pageable)
                .map(formationMapper::toListResponse);
    }

    @Transactional
    public void deleteFormation(UUID id) {
        Formation formation = formationRepository.findById(id)
                .orElseThrow(() -> CustomResponseException.ResourceNotFound("Training not found"));


        formation.setIsActive(false);
        formationRepository.save(formation);
        log.info("Deleted training (soft delete): {}", id);
    }
}
