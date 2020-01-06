package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.Comment;
import com.akulinski.r8meservice.domain.CommentID;
import com.akulinski.r8meservice.domain.CommentXProfile;
import com.akulinski.r8meservice.repository.CommentRepository;
import com.akulinski.r8meservice.repository.CommentXProfileRepository;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.repository.search.CommentSearchRepository;
import com.akulinski.r8meservice.security.SecurityUtils;
import com.akulinski.r8meservice.service.dto.CommentDTO;
import com.akulinski.r8meservice.service.mapper.CommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link Comment}.
 */
@Service
@Transactional
public class CommentService {

    private final Logger log = LoggerFactory.getLogger(CommentService.class);

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final CommentSearchRepository commentSearchRepository;

    private final CommentXProfileRepository commentXProfileRepository;

    private final UserProfileRepository userProfileRepository;

    private final UserRepository userRepository;


    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, CommentSearchRepository commentSearchRepository, UserProfileRepository userProfileRepository, UserRepository userRepository, CommentXProfileRepository commentXProfileRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.commentSearchRepository = commentSearchRepository;
        this.commentXProfileRepository = commentXProfileRepository;
        this.userProfileRepository = userProfileRepository;
        this.userRepository = userRepository;
    }

    /**
     * Save a comment.
     *
     * @param commentDTO the entity to save.
     * @return the persisted entity.
     */
    @Transactional
    public CommentDTO save(CommentDTO commentDTO) {
        final var receiver = userProfileRepository.findByUser_Login(commentDTO.getReceiver()).orElseThrow(() -> new UsernameNotFoundException(commentDTO.getReceiver()));
        final var poster = userProfileRepository.findByUser_Login(SecurityUtils.getCurrentUserLogin()
            .orElseThrow(() -> new IllegalStateException("Username not present")))
            .orElseThrow(() -> new UsernameNotFoundException(commentDTO.getReceiver()));

        if (commentDTO.getTimeStamp() == null)
            commentDTO.setTimeStamp(Instant.now());

        Comment comment = commentMapper.toEntity(commentDTO);
        comment = commentRepository.save(comment);

        CommentID commentID = new CommentID(receiver.getId(), poster.getId(), comment.getId());

        log.debug("Request to save Comment : {}", commentDTO);
        CommentDTO result = commentMapper.toDto(comment);
        commentSearchRepository.save(comment);

        CommentXProfile commentXProfile = new CommentXProfile();
        commentXProfile.setComment(comment);
        commentXProfile.setPoster(poster);
        commentXProfile.setReceiver(receiver);
        commentXProfile.setCommentID(commentID);

        result.setPoster(poster.getUser().getLogin());
        result.setImageUrl(poster.getUser().getImageUrl());
        result.setReceiver(receiver.getUser().getLogin());
        result.setTimeStamp(commentDTO.getTimeStamp());
        commentXProfileRepository.save(commentXProfile);
        return result;
    }

    /**
     * Get all the comments.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> findAll() {
        log.debug("Request to get all Comments");
        return commentRepository.findAll().stream()
            .map(commentMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Get one comment by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<CommentDTO> findOne(Long id) {
        log.debug("Request to get Comment : {}", id);
        return commentRepository.findById(id)
            .map(commentMapper::toDto);
    }

    /**
     * Delete the comment by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete Comment : {}", id);
        commentRepository.deleteById(id);
        commentSearchRepository.deleteById(id);
    }

    /**
     * Search for the comment corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<CommentDTO> search(String query) {
        log.debug("Request to search Comments for query {}", query);
        return StreamSupport
            .stream(commentSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(commentMapper::toDto)
            .collect(Collectors.toList());
    }

    public List<CommentDTO> findCommentsForUser() {
        final var username = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("NO username for current user"));
        final var user = userRepository.findOneByLogin(username).orElseThrow(() -> new IllegalStateException(String.format("No user for username: %s", username)));
        final var userProfile = userProfileRepository.findByUser(user).orElseThrow(() -> new IllegalStateException(String.format("No profile connected to user: %s", username)));

        log.debug("Request to find all comments for user {}", username);

        List<CommentXProfile> allByReceiver = commentXProfileRepository.findAllByReceiver(userProfile);

        return allByReceiver.stream().map(commentXProfile -> {
            final var commentDTO = new CommentDTO();
            try {
                final var comment = commentRepository.findByCommentXProfile(commentXProfile)
                    .orElseThrow(() -> new IllegalStateException(String.format("No comment connected to commentXProfile, DB inconsistency detected for: %s", commentXProfile.toString())));

                commentDTO.setImageUrl(commentXProfile.getPoster().getUser().getImageUrl());
                commentDTO.setId(comment.getId());
                commentDTO.setReceiver(commentXProfile.getReceiver().getUser().getLogin());
                commentDTO.setComment(comment.getComment());
                commentDTO.setTimeStamp(comment.getTimeStamp());
                commentDTO.setPoster(commentXProfile.getPoster().getUser().getLogin());
                return commentDTO;
            } catch (RuntimeException ex) {
                log.warn(ex.getLocalizedMessage());
            }
            commentDTO.setTimeStamp(Instant.now());
            return commentDTO;
        }).sorted().collect(Collectors.toList());
    }
}
