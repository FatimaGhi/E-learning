package dcc.formationservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateSessionRequest {
    private String name;
    private String description;
    private Integer orderIndex;
    private String type;
    private String contentUrl;
    private Integer durationMinutes;
    private String qcmData;
    private Integer passingScore;
    private Boolean isActive;
}
