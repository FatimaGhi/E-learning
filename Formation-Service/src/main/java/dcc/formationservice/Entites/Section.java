package dcc.formationservice.Entites;


import jakarta.persistence.Column;
import jakarta.persistence.Id;

import java.util.UUID;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "sections")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID id ;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private Integer orderIndex; // display order

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "formation_id", nullable = false)
    private Formation formation;

    @OneToMany(mappedBy = "section", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("orderIndex ASC")
    @Builder.Default
    private List<Session> sessions = new ArrayList<>();

    @CreationTimestamp
    private LocalDateTime createdAt;

    // Helper methods
    public void addSession(Session session) {
        sessions.add(session);
        session.setSection(this);
    }

    public void removeSession(Session session) {
        sessions.remove(session);
        session.setSection(null);
    }
}
