package dcc.formationservice.Repo;

import dcc.formationservice.Entites.Section;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface SectionRepository extends JpaRepository<Section, UUID> {


    List<Section> findByFormationIdOrderByOrderIndexAsc(UUID formationId);

    Optional<Section> findByIdAndFormationId(UUID id, Long formationId);

    @Query("SELECT COALESCE(MAX(s.orderIndex), -1) FROM Section s WHERE s.formation.id = :formationId")
    Integer findMaxOrderIndexByFormationId(@Param("formationId") Long formationId);
}
