package dcc.formationservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateFormationRequest {

    private String name;
    private String formatterName;
    private String description;
    private String objectif;
    private Integer durationHours;
    private BigDecimal price;
    private String promoCode;
    private BigDecimal discountPercentage;
    private Long categoryId;
    private Boolean isActive;
}
