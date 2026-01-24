package dcc.formationservice.Repo;

import dcc.formationservice.Entites.Session;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SessionRepository extends JpaRepository<Session, UUID> {
    List<Session> findBySectionIdOrderByOrderIndexAsc(UUID sectionId);

    Optional<Session> findByIdAndSectionId(UUID id, Long sectionId);

    @Query("SELECT COALESCE(MAX(s.orderIndex), -1) FROM Session s WHERE s.section.id = :sectionId")
    Integer findMaxOrderIndexBySectionId(@Param("sectionId") Long sectionId);

    List<Session> findBySectionIdAndIsActiveTrue(Long sectionId);
}
