package com.akulinski.r8meservice.service;

import com.akulinski.r8meservice.domain.FollowerXFollowed;
import com.akulinski.r8meservice.repository.FollowerXFollowedRepository;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.repository.search.FollowerXFollowedSearchRepository;
import com.akulinski.r8meservice.service.dto.FollowerXFollowedDTO;
import com.akulinski.r8meservice.service.mapper.FollowerXFollowedMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing {@link FollowerXFollowed}.
 */
@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class FollowerXFollowedService {

    private final FollowerXFollowedRepository followerXFollowedRepository;

    private final FollowerXFollowedMapper followerXFollowedMapper;

    private final FollowerXFollowedSearchRepository followerXFollowedSearchRepository;

    private final UserProfileRepository userProfileRepository;

    private final UserRepository userRepository;

    /**
     * Save a followerXFollowed.
     *
     * @param followerXFollowedDTO the entity to save.
     * @return the persisted entity.
     */
    public FollowerXFollowedDTO save(FollowerXFollowedDTO followerXFollowedDTO) {
        log.debug("Request to save FollowerXFollowed : {}", followerXFollowedDTO);
        FollowerXFollowed followerXFollowed = followerXFollowedMapper.toEntity(followerXFollowedDTO);
        followerXFollowed = followerXFollowedRepository.save(followerXFollowed);
        FollowerXFollowedDTO result = followerXFollowedMapper.toDto(followerXFollowed);
        followerXFollowedSearchRepository.save(followerXFollowed);
        return result;
    }

    /**
     * Get all the followerXFolloweds.
     *
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FollowerXFollowedDTO> findAll() {
        log.debug("Request to get all FollowerXFolloweds");
        return followerXFollowedRepository.findAll().stream()
            .map(followerXFollowedMapper::toDto)
            .collect(Collectors.toCollection(LinkedList::new));
    }


    /**
     * Follow action
     *
     * @param followerXFollowedDTO
     */
    @Transactional
    public void follow(FollowerXFollowedDTO followerXFollowedDTO) {

        final var followedUser = userRepository.findOneByLogin(followerXFollowedDTO.getFollowedUsername()).orElseThrow(ExceptionUtils.getNoUserFoundExceptionSupplier(followerXFollowedDTO.getFollowedUsername()));

        final var followerUser = userRepository.findOneByLogin(followerXFollowedDTO.getFollowerUsername()).orElseThrow(ExceptionUtils.getNoUserFoundExceptionSupplier(followerXFollowedDTO.getFollowerUsername()));

        final var followerXFollowed = new FollowerXFollowed();

        followerXFollowed.setFollowed(userProfileRepository.findByUser(followedUser).orElseThrow(ExceptionUtils.getNoProfileConnectedExceptionSupplier(followedUser.getId())));

        followerXFollowed.setFollower(userProfileRepository.findByUser(followerUser).orElseThrow(ExceptionUtils.getNoProfileConnectedExceptionSupplier(followedUser.getId())));

        followerXFollowedRepository.save(followerXFollowed);
        followerXFollowedSearchRepository.save(followerXFollowed);

        log.info("User: {} now follows: {}", followerUser.getLogin(), followedUser.getLogin());
    }

    /**
     * Get one followerXFollowed by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<FollowerXFollowedDTO> findOne(Long id) {
        log.debug("Request to get FollowerXFollowed : {}", id);
        return followerXFollowedRepository.findById(id)
            .map(followerXFollowedMapper::toDto);
    }

    /**
     * Delete the followerXFollowed by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        log.debug("Request to delete FollowerXFollowed : {}", id);
        followerXFollowedRepository.deleteById(id);
        followerXFollowedSearchRepository.deleteById(id);
    }

    /**
     * Search for the followerXFollowed corresponding to the query.
     *
     * @param query the query of the search.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public List<FollowerXFollowedDTO> search(String query) {
        log.debug("Request to search FollowerXFolloweds for query {}", query);
        return StreamSupport
            .stream(followerXFollowedSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .map(followerXFollowedMapper::toDto)
            .collect(Collectors.toList());
    }
}
