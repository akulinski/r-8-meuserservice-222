package com.akulinski.r8meservice.repository;
import com.akulinski.r8meservice.domain.CommentXProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;


/**
 * Spring Data  repository for the CommentXProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface CommentXProfileRepository extends JpaRepository<CommentXProfile, Long> {

}
