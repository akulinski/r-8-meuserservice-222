package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.security.AuthoritiesConstants;
import com.akulinski.r8meservice.service.CommentService;
import com.akulinski.r8meservice.service.dto.CommentDTO;
import com.akulinski.r8meservice.web.rest.errors.BadRequestAlertException;
import io.github.jhipster.web.util.HeaderUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;

/**
 * REST controller for managing {@link com.akulinski.r8meservice.domain.Comment}.
 */
@RestController
@RequestMapping("/api")
public class CommentResource {

    private final Logger log = LoggerFactory.getLogger(CommentResource.class);

    private static final String ENTITY_NAME = "comment";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CommentService commentService;

    public CommentResource(CommentService commentService) {
        this.commentService = commentService;
    }

    /**
     * {@code POST  /comments} : Create a new comment.
     *
     * @param commentDTO the commentDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new commentDTO, or with status {@code 400 (Bad Request)} if the comment has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/comments")
    public ResponseEntity<CommentDTO> createComment(@RequestBody CommentDTO commentDTO) throws URISyntaxException {
        log.debug("REST request to save Comment : {}", commentDTO);
        if (commentDTO.getId() != null) {
            throw new BadRequestAlertException("A new comment cannot already have an ID", ENTITY_NAME, "idexists");
        }
        CommentDTO result = commentService.save(commentDTO);
        return ResponseEntity.created(new URI("/api/comments/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /comments} : Updates an existing comment.
     *
     * @param commentDTO the commentDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated commentDTO,
     * or with status {@code 400 (Bad Request)} if the commentDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the commentDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/comments")
    public ResponseEntity<CommentDTO> updateComment(@RequestBody CommentDTO commentDTO) throws URISyntaxException {
        log.debug("REST request to update Comment : {}", commentDTO);
        if (commentDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        CommentDTO result = commentService.save(commentDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, commentDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /comments} : get all the comments.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comments in body.
     */
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("/comments/all")
    public List<CommentDTO> getAllComments() {
        log.debug("REST request to get all Comments");
        return commentService.findAll();
    }


    /**
     * {@code GET  /comments} : get all the comments.
     *

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of comments in body.
     */
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    @GetMapping("/comments")
    public List<CommentDTO> getComments() {

        log.debug("REST request to get all Comments");
        return commentService.findAll();
    }

    @GetMapping("/comments/comments-for-user")
    @ResponseStatus(HttpStatus.OK)
    public List<CommentDTO> getCommentsForUser(){
        return commentService.findCommentsForUser();
    }

}
