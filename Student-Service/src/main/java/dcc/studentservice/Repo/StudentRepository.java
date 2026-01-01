package dcc.studentservice.Repo;

import dcc.studentservice.Entities.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface StudentRepository extends JpaRepository<Student, UUID> {


    Optional<Student> findByEmail(String email);

    Optional<Student> findByKeycloakUserId(UUID keycloakUserId);
}
