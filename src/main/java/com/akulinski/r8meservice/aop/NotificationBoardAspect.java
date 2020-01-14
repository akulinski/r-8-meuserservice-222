package com.akulinski.r8meservice.aop;

import com.akulinski.r8meservice.domain.NotificaitonBoard;
import com.akulinski.r8meservice.domain.Question;
import com.akulinski.r8meservice.repository.FollowerXFollowedRepository;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.search.NotificationBoardRepository;
import com.akulinski.r8meservice.service.ExceptionUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import java.util.List;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class NotificationBoardAspect {

    private final NotificationBoardRepository notificationBoardRepository;

    private final UserProfileRepository userProfileRepository;

    private final FollowerXFollowedRepository followerXFollowedRepository;

    @AfterReturning(value = "execution(* com.akulinski.r8meservice.repository.search.QuestionSearchRepository.save(..))", returning = "question")
    public void afterSave(JoinPoint joinPoint, Question question) {
        final var poster = question.getPoster();

        final var posterProfile = userProfileRepository.findById(poster).orElseThrow(ExceptionUtils.getNoProfileConnectedExceptionSupplier(poster));

        final var allByFollowed = followerXFollowedRepository.findAllByFollowed(posterProfile);

        allByFollowed.forEach(followerXFollowed -> {
            final var follower = followerXFollowed.getFollower();

            notificationBoardRepository.findByProfileId(follower.getId()).ifPresentOrElse(notificaitonBoard -> {
                notificaitonBoard.getQuestionList().add(question);
                notificationBoardRepository.save(notificaitonBoard);
            }, () -> {
                NotificaitonBoard notificaitonBoard = new NotificaitonBoard();
                notificaitonBoard.setProfileId(follower.getId());
                notificaitonBoard.setQuestionList(List.of(question));
                notificationBoardRepository.save(notificaitonBoard);
            });
        });
    }

}
