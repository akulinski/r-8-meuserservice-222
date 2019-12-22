package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.Comment;
import com.akulinski.r8meservice.repository.CommentRepository;
import com.akulinski.r8meservice.repository.search.CommentSearchRepository;
import com.akulinski.r8meservice.service.dto.CommentDTO;
import com.akulinski.r8meservice.service.mapper.CommentMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

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

    public CommentService(CommentRepository commentRepository, CommentMapper commentMapper, CommentSearchRepository commentSearchRepository) {
        this.commentRepository = commentRepository;
        this.commentMapper = commentMapper;
        this.commentSearchRepository = commentSearchRepository;
    }

    /**
     * Save a comment.
     *
     * @param commentDTO the entity to save.
     * @return the persisted entity.
     */
    public CommentDTO save(CommentDTO commentDTO) {
        log.debug("Request to save Comment : {}", commentDTO);
        Comment comment = commentMapper.toEntity(commentDTO);
        comment = commentRepository.save(comment);
        CommentDTO result = commentMapper.toDto(comment);
        commentSearchRepository.save(comment);
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
    *  Get all the comments where CommentXProfile is {@code null}.
     *  @return the list of entities.
     */
    @Transactional(readOnly = true) 
    public List<CommentDTO> findAllWhereCommentXProfileIsNull() {
        log.debug("Request to get all comments where CommentXProfile is null");
        return StreamSupport
            .stream(commentRepository.findAll().spliterator(), false)
            .filter(comment -> comment.getCommentXProfile() == null)
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
}
