package dcc.formationservice.DTO;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateFormationRequest {

    @NotBlank(message = "name is required")
    @Size(min = 3, max = 200, message = "The name must contain between 3 and 200 characters")
    private String name;

    @NotBlank(message = "The name of the trainer is mandatory")
    private String formatterName;

    @NotBlank(message = "La description est obligatoire")
    @Size(min = 10, message = "The description must contain at least 10 characters")
    private String description;

    @NotBlank(message = "The objective is mandatory")
    private String objectif;

    @NotNull(message = "The duration is mandatory")
    @Min(value = 1, message = "The duration must be at least 1 hour")
    private Integer durationHours;

    @NotNull(message = "The price is mandatory")
    @DecimalMin(value = "0.0", inclusive = false, message = "The price must be positive")
    private BigDecimal price;

    private String promoCode;

    @DecimalMin(value = "0.0", message = "The reduction must be positive")
    @DecimalMax(value = "100.0", message = "The discount cannot exceed 100%")
    private BigDecimal discountPercentage;

    @NotNull(message = "The category is mandatory")
    private Long categoryId;
}
