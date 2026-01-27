package dcc.enrollmentservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SectionProgressResponse {
    private Long id;
    private Long sectionId;
    private String sectionTitle;
    private Integer sectionOrder;
    private Integer totalSessions;
    private Integer completedSessions;
    private Double progressPercentage;
    private Boolean isCompleted;
    private List<SessionProgressResponse> sessionProgresses;
}
