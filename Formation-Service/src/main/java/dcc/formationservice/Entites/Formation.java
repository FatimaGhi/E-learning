package dcc.formationservice.Entites;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "formations")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Formation {


    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    @GeneratedValue(generator = "UUID")
    private UUID id ;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String formatterName;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private String imageUrl; // URL MinIO

    @Column(columnDefinition= "TEXT")
    private String objectif;

    private Integer durationHours;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    private String promoCode;

    @Column(precision = 5, scale = 2)
    private BigDecimal discountPercentage; // percentage reduction

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @OneToMany(mappedBy = "formation", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Section> sections = new ArrayList<>();

    @Column(nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;

    // Helper methods
    public void addSection(Section section) {
        sections.add(section);
        section.setFormation(this);
    }

    public void removeSection(Section section) {
        sections.remove(section);
        section.setFormation(null);
    }
}
