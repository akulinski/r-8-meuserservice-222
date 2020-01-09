package com.akulinski.r8meservice.config;

import com.akulinski.r8meservice.domain.*;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.repository.search.CommentSearchRepository;
import com.akulinski.r8meservice.repository.search.QuestionSearchRepository;
import com.github.javafaker.Faker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Configuration
@Profile("mock-data")
@Slf4j
@RequiredArgsConstructor
public class FakerConfig {

    private Faker faker = new Faker();

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final CommentSearchRepository commentSearchRepository;

    private final QuestionSearchRepository questionSearchRepository;

    private Random random = new Random();

    @Value("${mock.comments}")
    private long comments;

    @Value("${mock.ratesPerQuestion}")
    private long ratesPerQuestion;

    @Value("${mock.questions}")
    private long questions;

    @EventListener(ApplicationReadyEvent.class)
    public void mockData() {

        List<User> emptyProfiles = userRepository.findAll().stream().filter(user -> userProfileRepository.findByUser(user).isEmpty()).collect(Collectors.toList());
        emptyProfiles.forEach(this::createUserProfile);
        emptyProfiles.forEach(this::setUpRatesAndComments);
    }

    private UserProfile setUpRatesAndComments(User user) {
        final List<User> users = userRepository.findAll();
        final var userProfile = userProfileRepository.findByUser(user).get();
        for (int j = 0; j < comments; j++) {
            try {
                User randomUser = users.get(random.nextInt(users.size() - 1));
                UserProfile randomProfile = userProfileRepository.findByUser(randomUser).orElse(null);

                while (randomProfile == null) {
                    randomUser = users.get(random.nextInt(users.size() - 1));
                    randomProfile = userProfileRepository.findByUser(randomUser).orElse(null);

                }

                setUpComment(randomProfile, userProfile);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage());
            }
        }

        for (int j = 0; j < questions; j++) {
            try {
                User randomUser = users.get(random.nextInt(users.size() - 1));
                UserProfile randomProfile = userProfileRepository.findByUser(randomUser).orElse(null);

                while (randomProfile == null) {
                    randomUser = users.get(random.nextInt(users.size() - 1));
                    randomProfile = userProfileRepository.findByUser(randomUser).orElse(null);

                }

                setUpRate(randomProfile, userProfile);
            } catch (Exception ex) {
                log.error(ex.getLocalizedMessage());
            }
        }

        return userProfile;
    }

    private UserProfile createUserProfile(User user) {
        final var userProfile = new UserProfile();
        userProfile.setUser(user);
        userProfileRepository.save(userProfile);
        return userProfile;
    }

    private void setUpRate(UserProfile randomProfile, UserProfile userProfile) {

        Question question = new Question();
        question.setContent(faker.lordOfTheRings().location());
        question.setLink(faker.avatar().image());
        question.setPoster(userProfile.getId());

        for(int i=0;i<ratesPerQuestion;i++){
            Rate rate = new Rate();
            rate.setReceiver(userProfile.getId());
            rate.setValue(faker.random().nextDouble());
            rate.setPoster(randomProfile.getId());
            question.getRates().add(rate);
            log.debug(rate.toString());
        }

        log.debug(question.toString());
        questionSearchRepository.save(question);
    }

    private void setUpComment(UserProfile poster, UserProfile userProfile) {
        try {
            Comment comment = new Comment();
            comment.setComment(faker.witcher().quote());
            comment.setPoster(poster.getId());
            comment.setReceiver(userProfile.getId());
            commentSearchRepository.save(comment);
        } catch (Exception ex) {
            log.error(ex.getLocalizedMessage());
        }
    }
}
