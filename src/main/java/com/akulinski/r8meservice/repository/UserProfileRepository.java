package com.akulinski.r8meservice.repository;
import com.akulinski.r8meservice.domain.User;
import com.akulinski.r8meservice.domain.UserProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import java.util.Optional;


/**
 * Spring Data  repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    String PROFILE_BY_USER_LOGIN = "ProfileByUserLogin";

    void deleteAllByUser(User user);

    Optional<UserProfile> findByUser(User user);
}
