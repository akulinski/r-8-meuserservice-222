package com.akulinski.r8meservice.repository;
import com.akulinski.r8meservice.domain.CommentXProfile;
import com.akulinski.r8meservice.domain.UserProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.List;


/**
 * Spring Data  repository for the CommentXProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentXProfileRepository extends JpaRepository<CommentXProfile, Long> {
    List<CommentXProfile> findAllByReceiver(UserProfile userProfile);
}
