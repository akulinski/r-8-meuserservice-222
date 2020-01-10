package com.akulinski.r8meservice.web.rest;

import com.akulinski.r8meservice.config.Constants;
import com.akulinski.r8meservice.domain.User;
import com.akulinski.r8meservice.domain.UserProfile;
import com.akulinski.r8meservice.repository.FollowerXFollowedRepository;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.repository.search.CommentSearchRepository;
import com.akulinski.r8meservice.repository.search.UserProfileSearchRepository;
import com.akulinski.r8meservice.repository.search.UserSearchRepository;
import com.akulinski.r8meservice.security.AuthoritiesConstants;
import com.akulinski.r8meservice.security.SecurityUtils;
import com.akulinski.r8meservice.service.MailService;
import com.akulinski.r8meservice.service.PhotoStorageService;
import com.akulinski.r8meservice.service.UserService;
import com.akulinski.r8meservice.service.dto.UserDTO;
import com.akulinski.r8meservice.web.rest.errors.BadRequestAlertException;
import com.akulinski.r8meservice.web.rest.errors.EmailAlreadyUsedException;
import com.akulinski.r8meservice.web.rest.errors.LoginAlreadyUsedException;
import com.akulinski.r8meservice.web.rest.vm.PhotoVM;
import com.akulinski.r8meservice.web.rest.vm.UserProfileVM;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

/**
 * REST controller for managing users.
 * <p>
 * This class accesses the {@link User} entity, and needs to fetch its collection of authorities.
 * <p>
 * For a normal use-case, it would be better to have an eager relationship between User and Authority,
 * and send everything to the client side: there would be no View Model and DTO, a lot less code, and an outer-join
 * which would be good for performance.
 * <p>
 * We use a View Model and a DTO for 3 reasons:
 * <ul>
 * <li>We want to keep a lazy association between the user and the authorities, because people will
 * quite often do relationships with the user, and we don't want them to get the authorities all
 * the time for nothing (for performance reasons). This is the #1 goal: we should not impact our users'
 * application because of this use-case.</li>
 * <li> Not having an outer join causes n+1 requests to the database. This is not a real issue as
 * we have by default a second-level cache. This means on the first HTTP call we do the n+1 requests,
 * but then all authorities come from the cache, so in fact it's much better than doing an outer join
 * (which will get lots of data from the database, for each HTTP call).</li>
 * <li> As this manages users, for security reasons, we'd rather have a DTO layer.</li>
 * </ul>
 * <p>
 * Another option would be to have a specific JPA entity graph to handle this case.
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Slf4j
public class UserResource {

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserService userService;

    private final UserRepository userRepository;

    private final UserProfileRepository userProfileRepository;

    private final FollowerXFollowedRepository followerXFollowedRepository;

    private final UserProfileSearchRepository userProfileSearchRepository;

    private final MailService mailService;

    private final UserSearchRepository userSearchRepository;

    private final PhotoStorageService photoStorageService;

    private final CommentSearchRepository commentSearchRepository;

    /**
     * {@code POST  /users}  : Creates a new user.
     * <p>
     * Creates a new user if the login and email are not already used, and sends an
     * mail with an activation link.
     * The user needs to be activated on creation.
     *
     * @param userDTO the user to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new user, or with status {@code 400 (Bad Request)} if the login or email is already in use.
     * @throws URISyntaxException       if the Location URI syntax is incorrect.
     * @throws BadRequestAlertException {@code 400 (Bad Request)} if the login or email is already in use.
     */
    @PostMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<User> createUser(@Valid @RequestBody UserDTO userDTO) throws URISyntaxException {
        log.debug("REST request to save User : {}", userDTO);

        if (userDTO.getId() != null) {
            throw new BadRequestAlertException("A new user cannot already have an ID", "userManagement", "idexists");
            // Lowercase the user login before comparing with database
        } else if (userRepository.findOneByLogin(userDTO.getLogin().toLowerCase()).isPresent()) {
            throw new LoginAlreadyUsedException();
        } else if (userRepository.findOneByEmailIgnoreCase(userDTO.getEmail()).isPresent()) {
            throw new EmailAlreadyUsedException();
        } else {
            User newUser = userService.createUser(userDTO);
            mailService.sendCreationEmail(newUser);
            return ResponseEntity.created(new URI("/api/users/" + newUser.getLogin()))
                .headers(HeaderUtil.createAlert(applicationName, "A user is created with identifier " + newUser.getLogin(), newUser.getLogin()))
                .body(newUser);
        }
    }

    /**
     * {@code PUT /users} : Updates an existing User.
     *
     * @param userDTO the user to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated user.
     * @throws EmailAlreadyUsedException {@code 400 (Bad Request)} if the email is already in use.
     * @throws LoginAlreadyUsedException {@code 400 (Bad Request)} if the login is already in use.
     */
    @PutMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> updateUser(@Valid @RequestBody UserDTO userDTO) {
        log.debug("REST request to update User : {}", userDTO);
        Optional<User> existingUser = userRepository.findOneByEmailIgnoreCase(userDTO.getEmail());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new EmailAlreadyUsedException();
        }
        existingUser = userRepository.findOneByLogin(userDTO.getLogin().toLowerCase());
        if (existingUser.isPresent() && (!existingUser.get().getId().equals(userDTO.getId()))) {
            throw new LoginAlreadyUsedException();
        }
        Optional<UserDTO> updatedUser = userService.updateUser(userDTO);

        return ResponseUtil.wrapOrNotFound(updatedUser,
            HeaderUtil.createAlert(applicationName, "A user is updated with identifier " + userDTO.getLogin(), userDTO.getLogin()));
    }

    /**
     * {@code GET /users} : get all users.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body all users.
     */
    @GetMapping("/users")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<List<UserDTO>> getAllUsers(Pageable pageable) {
        final Page<UserDTO> page = userService.getAllManagedUsers(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * Gets a list of all roles.
     *
     * @return a string list of all roles.
     */
    @GetMapping("/users/authorities")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public List<String> getAuthorities() {
        return userService.getAuthorities();
    }

    /**
     * {@code GET /users/:login} : get the "login" user.
     *
     * @param login the login of the user to find.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the "login" user, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<UserDTO> getUser(@PathVariable String login) {
        log.debug("REST request to get User : {}", login);
        return ResponseUtil.wrapOrNotFound(
            userService.getUserWithAuthoritiesByLogin(login)
                .map(UserDTO::new));
    }

    /**
     * {@code DELETE /users/:login} : delete the "login" User.
     *
     * @param login the login of the user to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/users/{login:" + Constants.LOGIN_REGEX + "}")
    @PreAuthorize("hasRole(\"" + AuthoritiesConstants.ADMIN + "\")")
    public ResponseEntity<Void> deleteUser(@PathVariable String login) {
        log.debug("REST request to delete User: {}", login);
        userService.deleteUser(login);
        return ResponseEntity.noContent().headers(HeaderUtil.createAlert(applicationName, "A user is deleted with identifier " + login, login)).build();
    }

    /**
     * {@code SEARCH /_search/users/:query} : search for the User corresponding to the query.
     *
     * @param query the query to search.
     * @return the result of the search.
     */
    @GetMapping("/_search/users/{query}")
    public List<UserProfileVM> search(@PathVariable String query) {
        var byLogin = userSearchRepository.findByLoginFuzzy(query);

        if (byLogin.size() < 5) {
            Iterable<User> combinedIterables = Iterables.unmodifiableIterable(
                Iterables.concat(userSearchRepository.findByLoginFuzzy(query), userSearchRepository.findByRegex(query)));
            byLogin = Lists.newArrayList(combinedIterables);
        }

        return byLogin.stream()
            .distinct()
            .map(this::getUserProfileVM)
            .collect(Collectors.toList());
    }

    @GetMapping("/user")
    public ResponseEntity<UserProfileVM> getCurrentUser() {
        return ResponseEntity.ok(getUserProfileVM());
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<UserProfileVM> getUserByUsername(@PathVariable("username") String username) {
        return ResponseEntity.ok(getUserProfileVMFromLogin(username));
    }

    private UserProfileVM getUserProfileVM(User user) {

        final var profile = userProfileRepository.findByUser(user).orElseThrow(()->new IllegalStateException(String.format("No profile found for user: %d", user.getId())));

        return new UserProfileVM(user.getLogin(), profile.getCurrentRating(), user.getImageUrl(), followerXFollowedRepository.findAllByFollowed(profile).size(), commentSearchRepository.findAllByReceiver(profile.getId()).size());
    }

    @PostMapping("/user/upload-photo")
    public ResponseEntity<Void> uploadPhoto(@RequestParam("photo") MultipartFile multipartFile) {
        final User user = getUserFromContext();
        final var link = photoStorageService.storeProfilePicture(multipartFile, user);

        if (StringUtils.isNotEmpty(link)) {
            return ResponseEntity.created(URI.create(link)).build();
        } else {
            return ResponseEntity.badRequest().build();
        }
    }

    private User getUserFromContext() {
        final var username = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("No Username provided"));
        return userRepository.findOneByLogin(username).orElseThrow(() -> new UsernameNotFoundException(username));
    }

    @GetMapping("user/get-avatar")
    public ResponseEntity<PhotoVM> getAvatar() {
        final User user = getUserFromContext();
        return ResponseEntity.ok(new PhotoVM(photoStorageService.getLinkForUser(user)));
    }

    private UserProfileVM getUserProfileVM() {
        final var currentLogin = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("No login found"));
        return getUserProfileVMFromLogin(currentLogin);
    }

    private UserProfileVM getUserProfileVMFromLogin(String currentLogin) {
        final var currentUser = userRepository.findOneByLogin(currentLogin).orElseThrow(() -> new IllegalStateException(String.format("User not found with login: %s", currentLogin)));
        final var profile = userProfileRepository.findByUser(currentUser).orElseThrow(() -> new IllegalStateException(String.format("No profile is connected to user: %s", currentLogin)));

        return new UserProfileVM(currentLogin, profile.getCurrentRating(), currentUser.getImageUrl(), followerXFollowedRepository.findAllByFollowed(profile).size(), commentSearchRepository.findAllByReceiver(profile.getId()).size());
    }

}
