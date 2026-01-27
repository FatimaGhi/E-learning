package dcc.enrollmentservice.DTO;



import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CompleteSessionRequest {
    @NotNull(message = "is required")
    private Long sessionId;

    private Integer videoProgressSeconds;
}
