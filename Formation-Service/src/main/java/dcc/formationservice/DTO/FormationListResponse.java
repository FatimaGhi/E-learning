package dcc.formationservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FormationListResponse {
    private UUID id;
    private String name;
    private String formatterName;
    private String imageUrl;
    private Integer durationHours;
    private BigDecimal price;
    private BigDecimal discountPercentage;
    private CategoryResponse category;
    private Integer sectionsCount;
    private Boolean isActive;
    private LocalDateTime createdAt;
}
