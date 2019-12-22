package com.akulinski.r8meservice.repository;
import com.akulinski.r8meservice.domain.User;
import com.akulinski.r8meservice.domain.UserProfile;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;
import java.util.Optional;
import java.util.stream.Stream;

import static org.hibernate.jpa.QueryHints.HINT_FETCH_SIZE;


/**
 * Spring Data  repository for the UserProfile entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {
    String PROFILE_BY_USER_LOGIN = "ProfileByUserLogin";

    void deleteAllByUser(User user);

    Optional<UserProfile> findByUser(User user);

    Optional<UserProfile> findByUser_Login(String login);

    @QueryHints(value = @QueryHint(name = HINT_FETCH_SIZE, value = "" + 50))
    @Query("select up from UserProfile up")
    Stream<UserProfile> findAllStream();
}
