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
public class SectionProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "enrollment_id", nullable = false)
    private Enrollment enrollment;

    @Column(nullable = false)
    private Long sectionId;

    @Column(nullable = false)
    private String sectionTitle;

    @Column(nullable = false)
    private Integer sectionOrder;

    @Column(nullable = false)
    private Integer totalSessions;

    @Column(nullable = false)
    private Integer completedSessions = 0;

    @Column(nullable = false)
    private Double progressPercentage = 0.0;

    @Column(nullable = false)
    private Boolean isCompleted = false;

    @OneToMany(mappedBy = "sectionProgress", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SessionProgress> sessionProgresses = new ArrayList<>();

    public void addSessionProgress(SessionProgress sessionProgress) {
        sessionProgresses.add(sessionProgress);
        sessionProgress.setSectionProgress(this);
    }

    public void calculateProgress() {
        if (totalSessions == 0) {
            this.progressPercentage = 0.0;
            return;
        }
        this.progressPercentage = (completedSessions * 100.0) / totalSessions;
        this.isCompleted = completedSessions.equals(totalSessions);
    }

}
