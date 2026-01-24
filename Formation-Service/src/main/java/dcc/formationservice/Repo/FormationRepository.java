package dcc.formationservice.Repo;

import dcc.formationservice.Entites.Formation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Pageable;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface FormationRepository extends JpaRepository<Formation, UUID> {

    Page<Formation> findByIsActiveTrue(Pageable pageable);


    Page<Formation> findByCategoryIdAndIsActiveTrue( Long categoryId, Pageable pageable);

    Page<Formation> findByFormatterNameContainingIgnoreCaseAndIsActiveTrue(String formatterName, Pageable pageable);

    @Query("SELECT f FROM Formation f WHERE LOWER(f.name) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(f.description) LIKE LOWER(CONCAT('%', :keyword, '%')) AND f.isActive = true")
    Page<Formation> searchFormations(@Param("keyword") String keyword, Pageable pageable);

    Optional<Formation> findByIdAndIsActiveTrue(UUID id);

    boolean existsByPromoCode(String promoCode);
}
