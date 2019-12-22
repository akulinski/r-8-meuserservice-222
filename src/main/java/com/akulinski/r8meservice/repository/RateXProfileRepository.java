package com.akulinski.r8meservice.repository;
import com.akulinski.r8meservice.domain.Rate;
import com.akulinski.r8meservice.domain.RateXProfile;
import com.akulinski.r8meservice.domain.UserProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the RateXProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface RateXProfileRepository extends JpaRepository<RateXProfile, Long> {
    List<RateXProfile> findAllByRated(UserProfile userProfile);
    void deleteAllByRate(Rate rate);
}
