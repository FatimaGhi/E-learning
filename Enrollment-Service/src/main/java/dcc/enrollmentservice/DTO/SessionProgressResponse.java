package dcc.enrollmentservice.DTO;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SessionProgressResponse {

    private Long id;
    private Long sessionId;
    private String sessionTitle;
    private Integer sessionOrder;
    private Boolean isCompleted;
    private LocalDateTime startedAt;
    private LocalDateTime completedAt;
    private Integer videoProgressSeconds;
    private Integer videoDurationSeconds;
}
