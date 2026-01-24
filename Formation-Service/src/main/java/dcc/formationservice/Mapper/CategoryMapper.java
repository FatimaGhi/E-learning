package dcc.formationservice.Mapper;

import dcc.formationservice.DTO.CategoryResponse;
import dcc.formationservice.Entites.Category;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {
    public CategoryResponse toResponseCategory(Category category) {
        if (category == null) {
            return null;
        }

        return CategoryResponse.builder()
                .id(category.getId())
                .name(category.getName())
                .description(category.getDescription())
                .iconUrl(category.getIconUrl())
                .build();
    }
}
