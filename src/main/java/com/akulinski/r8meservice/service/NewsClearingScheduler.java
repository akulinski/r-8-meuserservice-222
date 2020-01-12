package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.*;
import com.akulinski.r8meservice.repository.SchedulerLogRepository;
import com.akulinski.r8meservice.repository.search.NotificationBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

/**
 * Scheduler that runs every half and hour and removes
 * old questions form notification board
 */
@RequiredArgsConstructor
@Slf4j
@Service
public class NewsClearingScheduler {

    private final NotificationBoardRepository notificationBoardRepository;

    private final SchedulerLogRepository schedulerLogRepository;

    //every half and hour
    @Scheduled(fixedDelay = 1800000)
    public void removeOldQuestionsFromBoard() {
        schedulerLogRepository.findFirstBySchedulerTypeOrderByTimestampDesc(SchedulerType.NEWS_REMOVAL)
            .ifPresentOrElse(this::checkLogTimestampAndStatus, this::tryToRemoveOldQuestionsAndLogResultToDB);

    }

    /**
     * Check if last run of scheduler was at least
     * 6hrs ago or later or if last run was failed and
     * ended with {@link Error} status
     *
     * @param schedulerLog
     */
    public void checkLogTimestampAndStatus(SchedulerLog schedulerLog) {
        final var schedulerLogTimeStamp = schedulerLog.getTimestamp();
        Instant now = Instant.now();

        if ((schedulerLogTimeStamp.isBefore(now.minus(6, ChronoUnit.HOURS))) &&
            (schedulerLogTimeStamp.isBefore(now)) ||
            (SchedulerStatus.ERROR.equals(schedulerLog.getSchedulerStatus()))) {
            tryToRemoveOldQuestionsAndLogResultToDB();
        }
    }

    /**
     * Creates new instance of {@link SchedulerLog}
     * and tries to remove all old questions
     */
    public void tryToRemoveOldQuestionsAndLogResultToDB() {
        SchedulerLog schedulerLog = new SchedulerLog();
        schedulerLog.setSchedulerType(SchedulerType.NEWS_REMOVAL);
        schedulerLog.setSchedulerStatus(SchedulerStatus.STARTED);
        schedulerLog = schedulerLogRepository.save(schedulerLog);

        log.info("Removing old questions from notification boards at {}", Instant.now());

        try {
            notificationBoardRepository.findAll().forEach(notificaitonBoard -> notificaitonBoard.getQuestionList().forEach(question -> {
                checkIfOldAndRemove(notificaitonBoard, question);
            }));
            log.info("Removing old questions from notification boards finished at {}", Instant.now());
            schedulerLog.setSchedulerStatus(SchedulerStatus.FINISHED);
            schedulerLogRepository.save(schedulerLog);

        } catch (Exception ex) {
            log.info("Removing old questions from notification boards failed at {} with exception {}", Instant.now(), ex);
            schedulerLog.setSchedulerStatus(SchedulerStatus.ERROR);
            schedulerLogRepository.save(schedulerLog);
        }
    }

    public void checkIfOldAndRemove(NotificaitonBoard notificaitonBoard, Question question) {
        final var questionTimeStamp = question.getTimeStamp();
        Instant now = Instant.now();

        if ((questionTimeStamp.isBefore(now.minus(2, ChronoUnit.WEEKS))) &&
            (questionTimeStamp.isBefore(now))) {
            notificaitonBoard.getQuestionList().remove(question);
            notificationBoardRepository.save(notificaitonBoard);
        }
    }
}
