package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.domain.Question;
import com.akulinski.r8meservice.service.NotificationBoardService;
import com.akulinski.r8meservice.service.UserProfileService;
import com.akulinski.r8meservice.web.rest.vm.QuestionNotificationVM;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/notifications")
public class NotificationBoardResource {

    private final NotificationBoardService notificationBoardService;

    private final UserProfileService userProfileService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionNotificationVM> getNotifications() {
        return notificationBoardService.getQuestionsForUser().stream()
            .map(this::getQuestionNotificationVMFromQuestion)
            .collect(Collectors.toList());
    }

    @GetMapping("/{username}")
    @ResponseStatus(HttpStatus.OK)
    public List<QuestionNotificationVM> getNotificationsForUser(@PathVariable("username") String username) {
        final var profileIdFromUsername = userProfileService.getProfileIdFromUsername(username);
        return notificationBoardService.getQuestionsForUser(profileIdFromUsername).stream()
            .map(this::getQuestionNotificationVMFromQuestion).collect(Collectors.toList());
    }

    public QuestionNotificationVM getQuestionNotificationVMFromQuestion(Question question) {
        QuestionNotificationVM questionNotificationVM = new QuestionNotificationVM();
        questionNotificationVM.setContent(question.getContent());
        questionNotificationVM.setUsername(userProfileService.getUsernameFromProfile(question.getPoster()));
        questionNotificationVM.setTimestamp(question.getTimeStamp());
        return questionNotificationVM;
    }
}
