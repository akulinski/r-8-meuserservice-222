package com.akulinski.r8meservice.config;

import com.akulinski.r8meservice.domain.*;
import com.akulinski.r8meservice.repository.*;
import com.akulinski.r8meservice.service.UserService;
import com.akulinski.r8meservice.service.dto.UserDTO;
import com.akulinski.r8meservice.service.util.RandomUtil;
import com.github.javafaker.Faker;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.event.EventListener;

import java.util.List;
import java.util.Random;
import java.util.Set;

@Profile("dev")
@Configuration
public class FakerConfig {
    private final Faker faker;
    private final UserRepository userRepository;
    private final UserProfileRepository userProfileRepository;
    private final CommentRepository commentRepository;
    private final CommentXProfileRepository commentXProfileRepository;
    private final RateRepository rateRepository;
    private final RateXProfileRepository rateXProfileRepository;
    private final UserService userService;
    private final Random random;

    public FakerConfig(UserRepository userRepository, UserProfileRepository userProfileRepository, CommentRepository commentRepository,
                       CommentXProfileRepository commentXProfileRepository, RateRepository rateRepository, RateXProfileRepository rateXProfileRepository, UserService userService) {
        this.userRepository = userRepository;
        this.userProfileRepository = userProfileRepository;
        this.commentRepository = commentRepository;
        this.commentXProfileRepository = commentXProfileRepository;
        this.rateRepository = rateRepository;
        this.rateXProfileRepository = rateXProfileRepository;
        this.userService = userService;
        this.random = new Random();
        this.faker = new Faker();
    }


    @EventListener(ApplicationReadyEvent.class)
    public void mockData() {
        if (userRepository.count() < 10) {

            for (int i = 0; i < 10; i++) {

                final List<User> users = userRepository.findAll();

                final UserDTO userDTO = getUserDTO();
                final var user = userService.createUser(userDTO);

                UserProfile userProfile = getUserProfile(user);
                userProfileRepository.save(userProfile);

                for(int j=0; j<10; j++){
                    try {
                        setUpComment(users, userProfile);
                        setUpRate(users, userProfile);
                    }catch (Exception ex){
                        //ignore
                    }
                }
            }
        }
    }

    private void setUpRate(List<User> users, UserProfile userProfile) {
        Rate rate = new Rate();
        rate.setQuestion(faker.witcher().location());
        rate.setValue(faker.random().nextDouble());
        rate = rateRepository.save(rate);

        RateXProfile rateXProfile = new RateXProfile();
        rateXProfile.setRated(userProfile);
        rateXProfile.setRater(userProfileRepository.findByUser(users.get(random.nextInt(users.size()-1))).orElse(null));
        rateXProfile.setRate(rate);
        rateXProfileRepository.save(rateXProfile);
    }

    private void setUpComment(List<User> users, UserProfile userProfile) {
        Comment comment = new Comment();
        comment.setComment(faker.witcher().quote());

        comment = commentRepository.save(comment);

        CommentXProfile commentXProfile = new CommentXProfile();
        commentXProfile.setReceiver(userProfile);
        commentXProfile.setPoster(userProfileRepository.findByUser(users.get(random.nextInt(users.size()-1))).orElse(null));
        commentXProfile.setComment(comment);
        commentXProfileRepository.save(commentXProfile);
    }

    private UserProfile getUserProfile(User user) {
        return userProfileRepository.findByUser(user).get();
    }

    private UserDTO getUserDTO() {
        final var userDTO = new UserDTO();
        userDTO.setAuthorities(Set.of("USER"));
        userDTO.setActivated(true);
        userDTO.setEmail(faker.internet().emailAddress()+RandomUtil.generateResetKey());
        userDTO.setImageUrl(faker.avatar().image());
        userDTO.setLogin(faker.name().username()+RandomUtil.generateActivationKey());
        return userDTO;
    }
}
