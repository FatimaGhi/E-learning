package dcc.formationservice.Entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sessions")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID id ;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer orderIndex; // Ordre dans la section

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SessionType type; // VIDEO, FILE, QCM

    // For VIDEO and FILE
    private String contentUrl; // URL MinIO

    private Integer durationMinutes; // Duration in minutes (for video)

    // Pour QCM
    @Column(columnDefinition = "TEXT")
    private String qcmData; // JSON containing the questions/answers

    private Integer passingScore; // Minimum score to pass the QCM
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    public enum SessionType {
        VIDEO,
        FILE,
        QCM
    }
}
