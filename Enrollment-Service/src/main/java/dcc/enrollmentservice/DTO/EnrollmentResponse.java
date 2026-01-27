package dcc.enrollmentservice.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EnrollmentResponse {
    private Long id;
    private String studentId;
    private Long formationId;
    private String formationTitle;
    private EnrollmentStatus status;
    private BigDecimal paidAmount;
    private Integer totalSections;
    private Integer totalSessions;
    private Integer completedSessions;
    private Double progressPercentage;
    private LocalDateTime enrolledAt;
    private LocalDateTime completedAt;
    private List<SectionProgressResponse> sectionProgresses;
}
