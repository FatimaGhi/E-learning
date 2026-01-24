package dcc.formationservice.DTO;


import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSectionRequest {

    @NotBlank(message = "The name of the section is mandatory")
    @Size(min = 3, max = 200, message = "The name must contain between 3 and 200 characters")
    private String name;

    private String description;

    @NotNull(message = "The order is mandatory")
    @Min(value = 0, message = "The order must be positive")
    private Integer orderIndex;

    @NotNull(message = "The training ID is mandatory")
    private UUID formationId;
}
