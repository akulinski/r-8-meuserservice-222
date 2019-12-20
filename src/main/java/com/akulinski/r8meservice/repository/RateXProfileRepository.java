package com.akulinski.r8meservice.repository;
import com.akulinski.r8meservice.domain.RateXProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the RateXProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RateXProfileRepository extends JpaRepository<RateXProfile, Long> {

}
