package com.akulinski.r8meservice.repository;
import com.akulinski.r8meservice.domain.FollowerXFollowed;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the FollowerXFollowed entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FollowerXFollowedRepository extends JpaRepository<FollowerXFollowed, Long> {

}
