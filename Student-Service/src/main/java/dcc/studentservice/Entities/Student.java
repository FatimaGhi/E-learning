package dcc.studentservice.Entities;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class Student {
    @Id
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private UUID id ;

    @Column(nullable = false, updatable = false)
    private UUID keycloakUserId;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;


    @Column(nullable = false)
    private String email;

    private LocalDate birthDate;

    private String phone;



    @Column(updatable = false)
    private LocalDate createdAt;

    private LocalDate updatedAt;

    @PrePersist
    protected void onCreate() {
        if (id == null) {
            id = UUID.randomUUID();
        }
        this.createdAt = LocalDate.now();

    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDate.now();
    }
}
