package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.Comment;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.repository.search.CommentSearchRepository;
import com.akulinski.r8meservice.security.SecurityUtils;
import com.akulinski.r8meservice.service.dto.CommentDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Service Implementation for managing {@link Comment}.
 */
@Service
@Transactional
@Slf4j
@RequiredArgsConstructor
public class CommentService {

    private final CommentSearchRepository commentSearchRepository;

    private final UserProfileRepository userProfileRepository;

    private final UserRepository userRepository;

    /**
     * Save a comment.
     *
     * @param commentDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public CommentDTO save(CommentDTO commentDTO) {
        final var receiver = userProfileRepository.findByUser_Login(commentDTO.getReceiver()).orElseThrow(ExceptionUtils.getNoUserFoundExceptionSupplier(commentDTO.getReceiver()));
        final var poster = userProfileRepository.findByUser_Login(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(ExceptionUtils.getNoLoginInContextExceptionSupplier()))
            .orElseThrow(() -> new UsernameNotFoundException(commentDTO.getReceiver()));

        if (commentDTO.getTimeStamp() == null)
            commentDTO.setTimeStamp(Instant.now());

        Comment comment = new Comment();
        comment.setComment(commentDTO.getComment());
        comment.setPoster(poster.getId());
        comment.setReceiver(receiver.getId());

        comment = commentSearchRepository.save(comment);

        commentDTO.setImageUrl(poster.getUser().getImageUrl());
        commentDTO.setPoster(poster.getUser().getLogin());
        commentDTO.setId(comment.getId());

        log.debug("Request to save Comment : {}", commentDTO);

        return commentDTO;
    }

    /**
     * Get all the comments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> findAll() {
        log.debug("Request to get all Comments");
        return commentSearchRepository.findAll().stream()
            .map(comment -> {
                CommentDTO commentDTO = new CommentDTO();
                commentDTO.setComment(comment.getComment());
                commentDTO.setTimeStamp(comment.getTimeStamp());
                return commentDTO;
            })
            .collect(Collectors.toCollection(LinkedList::new));
    }

    public List<CommentDTO> findCommentsForUser() {
        final var username = SecurityUtils.getCurrentUserLogin().orElseThrow(ExceptionUtils.getNoLoginInContextExceptionSupplier());
        return getCommentDTOSForUsername(username);
    }

    public List<CommentDTO> findCommentsForUser(String username) {
        return getCommentDTOSForUsername(username);
    }

    private List<CommentDTO> getCommentDTOSForUsername(String username) {
        final var user = userRepository.findOneByLogin(username).orElseThrow(ExceptionUtils.getNoUserFoundExceptionSupplier(username));

        final var userProfile = userProfileRepository.findByUser(user).orElseThrow(ExceptionUtils.getNoProfileConnectedExceptionSupplier(user.getId()));

        log.debug("Request to find all comments for user {}", username);

        List<Comment> allByReceiver = commentSearchRepository.findAllByReceiver(userProfile.getId());

        return allByReceiver.stream().map(comment -> {
            final var commentDTO = new CommentDTO();
            try {
                final var userProfile1 = userProfileRepository.findById(comment.getPoster()).orElseThrow(ExceptionUtils.getNoUserFoundExceptionSupplier(String.valueOf(comment.getPoster())));
                commentDTO.setImageUrl(userProfile1.getUser().getImageUrl());
                commentDTO.setId(comment.getId());
                commentDTO.setReceiver(userProfile.getUser().getLogin());
                commentDTO.setComment(comment.getComment());
                commentDTO.setTimeStamp(comment.getTimeStamp());
                commentDTO.setPoster(userProfile1.getUser().getLogin());
                return commentDTO;
            } catch (RuntimeException ex) {
                log.warn(ex.getLocalizedMessage());
            }
            commentDTO.setTimeStamp(Instant.now());
            return commentDTO;
        }).sorted(Collections.reverseOrder()).collect(Collectors.toList());
    }
}
