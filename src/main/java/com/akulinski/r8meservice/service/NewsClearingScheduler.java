package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.repository.search.NotificationBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

@RequiredArgsConstructor
@Slf4j
@Service
public class NewsClearingScheduler {

    private final NotificationBoardRepository notificationBoardRepository;

    //every half and hour
    @Scheduled(fixedDelay = 1800000)
    public void removeOldQuestionsFromBoard() {
        log.info("Removing old questions from notification boards at {}", Instant.now());
        notificationBoardRepository.findAll().forEach(notificaitonBoard -> notificaitonBoard.getQuestionList().forEach(question -> {
            final var questionTimeStamp = question.getTimeStamp();
            Instant now = Instant.now();

            if ((questionTimeStamp.isBefore(now.minus(2, ChronoUnit.WEEKS))) &&
                (questionTimeStamp.isBefore(now))) {
                notificaitonBoard.getQuestionList().remove(question);
                notificationBoardRepository.save(notificaitonBoard);
            }
        }));

        log.info("Removing old questions from notification boards finished at {}", Instant.now());

    }
}
