package dcc.formationservice.DTO;


import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateSessionRequest {


    @NotBlank(message = "The name of the session is mandatory")
    @Size(min = 3, max = 200, message = "The name must contain between 3 and 200 characters")
    private String name;

    private String description;

    @NotNull(message = "The order is mandatory")
    @Min(value = 0, message = "The order must be positive")
    private Integer orderIndex;

    @NotBlank(message = "The session type is mandatory")
    @Pattern(regexp = "VIDEO|FILE|QCM", message = "The type must be VIDEO, FILE or QCM")
    private String type;

    // Pour VIDEO et FILE
    private String contentUrl;
    private Integer durationMinutes;

    // Pour QCM
    private String qcmData;

    @Min(value = 0, message = "The minimum score must be positive")
    @Max(value = 100, message = "The maximum score is 100")
    private Integer passingScore;

    @NotNull(message = "The section ID is mandatory")
    private UUID sectionId;
}
