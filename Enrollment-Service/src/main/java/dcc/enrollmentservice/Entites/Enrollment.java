package dcc.enrollmentservice.Entites;


import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "enrollments")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String studentId; // Keycloak user ID

    @Column(nullable = false)
    private Long formationId;

    @Column(nullable = false)
    private String formationTitle;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EnrollmentStatus status;

    private BigDecimal paidAmount;

    @Column(nullable = false)
    private Integer totalSections;

    @Column(nullable = false)
    private Integer totalSessions;

    @Column(nullable = false)
    private Integer completedSessions = 0;

    @Column(nullable = false)
    private Double progressPercentage = 0.0;

    @OneToMany(mappedBy = "enrollment", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<SectionProgress> sectionProgresses = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime enrolledAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    private LocalDateTime completedAt;

    // Helper methods
    public void addSectionProgress(SectionProgress sectionProgress) {
        sectionProgresses.add(sectionProgress);
        sectionProgress.setEnrollment(this);
    }

    public void calculateProgress() {
        if (totalSessions == 0) {
            this.progressPercentage = 0.0;
            return;
        }
        this.progressPercentage = (completedSessions * 100.0) / totalSessions;

        if (completedSessions.equals(totalSessions) && this.status != EnrollmentStatus.COMPLETED) {
            this.status = EnrollmentStatus.COMPLETED;
            this.completedAt = LocalDateTime.now();
        }
    }
}
