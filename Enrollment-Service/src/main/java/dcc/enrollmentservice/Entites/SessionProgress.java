package dcc.enrollmentservice.Entites;



import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "session_progress")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SessionProgress {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_progress_id", nullable = false)
    private SectionProgress sectionProgress;

    @Column(nullable = false)
    private Long sessionId;

    @Column(nullable = false)
    private String sessionTitle;

    @Column(nullable = false)
    private Integer sessionOrder;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @CreationTimestamp
    private LocalDateTime startedAt;

    private LocalDateTime completedAt;

    private Integer videoProgressSeconds = 0;
    private Integer videoDurationSeconds;
}
