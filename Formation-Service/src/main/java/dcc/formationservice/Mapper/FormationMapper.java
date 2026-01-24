package dcc.formationservice.Mapper;

import dcc.formationservice.DTO.CreateFormationRequest;
import dcc.formationservice.DTO.FormationResponse;
import dcc.formationservice.DTO.UpdateFormationRequest;
import dcc.formationservice.DTO.FormationListResponse;
import dcc.formationservice.Entites.Category;
import dcc.formationservice.Entites.Formation;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class FormationMapper {

    private CategoryMapper categoryMapper;
    private SectionMapper sectionMapper;

    public FormationMapper(CategoryMapper categoryMapper, SectionMapper sectionMapper) {
        this.categoryMapper = categoryMapper;
        this.sectionMapper = sectionMapper;
    }

    public Formation toEntity(CreateFormationRequest request, Category category, String imageUrl) {
        return Formation.builder()
                .name(request.getName())
                .formatterName(request.getFormatterName())
                .description(request.getDescription())
                .imageUrl(imageUrl)
                .objectif(request.getObjectif())
                .durationHours(request.getDurationHours())
                .price(request.getPrice())
                .promoCode(request.getPromoCode())
                .discountPercentage(request.getDiscountPercentage())
                .category(category)
                .isActive(true)
                .build();
    }

    public void updateEntity(Formation formation, UpdateFormationRequest request, Category category) {
        if (request.getName() != null) {
            formation.setName(request.getName());
        }
        if (request.getFormatterName() != null) {
            formation.setFormatterName(request.getFormatterName());
        }
        if (request.getDescription() != null) {
            formation.setDescription(request.getDescription());
        }
        if (request.getObjectif() != null) {
            formation.setObjectif(request.getObjectif());
        }
        if (request.getDurationHours() != null) {
            formation.setDurationHours(request.getDurationHours());
        }
        if (request.getPrice() != null) {
            formation.setPrice(request.getPrice());
        }
        if (request.getPromoCode() != null) {
            formation.setPromoCode(request.getPromoCode());
        }
        if (request.getDiscountPercentage() != null) {
            formation.setDiscountPercentage(request.getDiscountPercentage());
        }
        if (request.getIsActive() != null) {
            formation.setIsActive(request.getIsActive());
        }
        if (category != null) {
            formation.setCategory(category);
        }
    }


    public FormationResponse toResponse(Formation formation) {
        return FormationResponse.builder()
                .id(formation.getId())
                .name(formation.getName())
                .formatterName(formation.getFormatterName())
                .description(formation.getDescription())
                .imageUrl(formation.getImageUrl())
                .objectif(formation.getObjectif())
                .durationHours(formation.getDurationHours())
                .price(formation.getPrice())
                .promoCode(formation.getPromoCode())
                .discountPercentage(formation.getDiscountPercentage())
                .category(categoryMapper.toResponseCategory(formation.getCategory()))
                .sections(formation.getSections().stream()
                        .map(sectionMapper::toResponse)
                        .collect(Collectors.toList()))
                .isActive(formation.getIsActive())
                .createdAt(formation.getCreatedAt())
                .updatedAt(formation.getUpdatedAt())
                .build();
    }


    public FormationListResponse toListResponse(Formation formation) {
        return FormationListResponse.builder()
                .id(formation.getId())
                .name(formation.getName())
                .formatterName(formation.getFormatterName())
                .imageUrl(formation.getImageUrl())
                .durationHours(formation.getDurationHours())
                .price(formation.getPrice())
                .discountPercentage(formation.getDiscountPercentage())
                .category(categoryMapper.toResponseCategory(formation.getCategory()))
                .sectionsCount(formation.getSections().size())
                .isActive(formation.getIsActive())
                .createdAt(formation.getCreatedAt())
                .build();
    }
}
