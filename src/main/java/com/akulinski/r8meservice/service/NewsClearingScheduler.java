package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.SchedulerLog;
import com.akulinski.r8meservice.domain.SchedulerStatus;
import com.akulinski.r8meservice.domain.SchedulerType;
import com.akulinski.r8meservice.repository.SchedulerLogRepository;
import com.akulinski.r8meservice.repository.search.NotificationBoardRepository;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

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

    @Value("${config.board.ttl}")
    private long boardTtl;

    @Value("${config.scheduler.news.timeBetween}")
    private long timeBetween;

    //every half and hour
    @Scheduled(fixedRateString = "${scheduler.newscleaingscheduler}")
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

        //if already running stop
        if (SchedulerStatus.STARTED.equals(schedulerLog.getSchedulerStatus()))
            return;


        if ((schedulerLogTimeStamp.isBefore(now.minus(timeBetween, ChronoUnit.HOURS))) &&
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
            Instant now = Instant.now();
            Lists.newArrayList(notificationBoardRepository.findAll()).forEach(notificaitonBoard -> {
                final var collect = notificaitonBoard.getQuestionList().stream()
                    .filter(question -> (question.getTimeStamp().isAfter(now.minus(boardTtl, ChronoUnit.DAYS)))
                        && (question.getTimeStamp().isBefore(now))).collect(Collectors.toList());
                notificaitonBoard.setQuestionList(collect);
                notificationBoardRepository.save(notificaitonBoard);
            });

            log.info("Removing old questions from notification boards finished at {}", Instant.now());
            schedulerLog.setSchedulerStatus(SchedulerStatus.FINISHED);
            schedulerLogRepository.save(schedulerLog);

        } catch (Exception ex) {
            log.error("Removing old questions from notification boards failed at {} with exception {}", Instant.now(), ex);
            schedulerLog.setSchedulerStatus(SchedulerStatus.ERROR);
            schedulerLogRepository.save(schedulerLog);
        }
    }
}
