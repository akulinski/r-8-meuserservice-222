package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.NotificaitonBoard;
import com.akulinski.r8meservice.domain.Question;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.search.NotificationBoardRepository;
import com.akulinski.r8meservice.security.SecurityUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationBoardService {

    private final NotificationBoardRepository notificationBoardRepository;

    private final UserProfileRepository userProfileRepository;

    public List<Question> getQuestionsForUser(long userProfileId) {
        return notificationBoardRepository.findByProfileId(userProfileId).orElseGet(() -> getNotificaitonBoard(userProfileId)).getQuestionList().stream().sorted(Comparator.comparing(Question::getTimeStamp)).collect(Collectors.toList()).subList(0, 50);
    }

    public NotificaitonBoard getNotificaitonBoard(long userProfileId) {
        NotificaitonBoard notificaitonBoard = new NotificaitonBoard();
        notificaitonBoard.setQuestionList(List.of());
        notificaitonBoard.setProfileId(userProfileId);
        notificaitonBoard = notificationBoardRepository.save(notificaitonBoard);
        return notificaitonBoard;
    }

    public List<Question> getQuestionsForUser() {
        final var username = SecurityUtils.getCurrentUserLogin().orElseThrow(ExceptionUtils.getNoLoginInContextExceptionSupplier());

        final var userProfile = userProfileRepository.findByUser_Login(username).orElseThrow(ExceptionUtils.getNoProfileConnectedExceptionSupplier(username));

        return notificationBoardRepository.findByProfileId(userProfile.getId())
            .orElseGet(() -> getNotificaitonBoard(userProfile.getId()))
            .getQuestionList();
    }
}
