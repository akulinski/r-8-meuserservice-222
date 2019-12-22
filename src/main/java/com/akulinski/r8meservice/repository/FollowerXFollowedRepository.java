package com.akulinski.r8meservice.repository;
import com.akulinski.r8meservice.domain.FollowerXFollowed;
import com.akulinski.r8meservice.domain.UserProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the FollowerXFollowed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FollowerXFollowedRepository extends JpaRepository<FollowerXFollowed, Long> {
    List<FollowerXFollowed> findAllByFollowed(UserProfile userProfile);
}
