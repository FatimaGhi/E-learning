package dcc.formationservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormationResponse {
    private UUID id;
    private String name;
    private String formatterName;
    private String description;
    private String imageUrl;
    private String objectif;
    private Integer durationHours;
    private BigDecimal price;
    private String promoCode;
    private BigDecimal discountPercentage;
    private CategoryResponse category;
    private List<SectionResponse> sections;
    private Boolean isActive;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
