package dcc.enrollmentservice.DTO;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentRequest {
    @NotNull(message = "ID Formation is required")
    private Long formationId;

    @Positive(message = "must be positive")
    private BigDecimal paidAmount;
}
