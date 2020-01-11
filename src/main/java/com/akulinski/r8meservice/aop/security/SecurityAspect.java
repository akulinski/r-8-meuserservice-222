package com.akulinski.r8meservice.aop.security;

import com.akulinski.r8meservice.domain.ProtectedResource;
import com.akulinski.r8meservice.repository.UserProfileRepository;
import com.akulinski.r8meservice.repository.UserRepository;
import com.akulinski.r8meservice.security.SecurityUtils;
import com.google.common.collect.Lists;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;


/**
 *  This Aspect is responsible for checking if user is
 *  Authorized to access resource.
 */

@Aspect
@Component
@Slf4j
@RequiredArgsConstructor
public class SecurityAspect {

    private final UserProfileRepository userProfileRepository;

    private final UserRepository userRepository;

    /**
     * Executed before methods annotated with {@link com.akulinski.r8meservice.domain.OwnerCheck}
     *
     * @param joinPoint
     */
    @SneakyThrows
    @Before("@annotation(com.akulinski.r8meservice.domain.OwnerCheck)")
    public void checkOwnership(JoinPoint joinPoint) {

        log.info("Ownership check");

        final var protectedResource = getProtectedResourceFromArray(joinPoint.getArgs());

        final var userProfileFromResource = userProfileRepository.findById(protectedResource.getOwner())
            .orElseThrow(() -> new IllegalStateException("No profile found with id: " + protectedResource.getOwner()));

        final var login = SecurityUtils.getCurrentUserLogin().orElseThrow(() -> new IllegalStateException("No login in security context"));
        final var userByLogin = userRepository.findOneByLogin(login).orElseThrow(() -> new IllegalStateException(String.format("No User by login: %s", login)));

        final var profileIdFromResource = userProfileFromResource.getUser().getId();

        if (!userByLogin.getId().equals(profileIdFromResource)) {
            log.error("User {} cannot access this resource", login);
            throw new UnauthorizedToAccessException(String.format("User: %s Cannot access this resource because it belongs to %s", login, userProfileFromResource.getUser().getLogin()));
        }

        log.info("Ownership check finished");
    }

    private ProtectedResource getProtectedResourceFromArray(Object[] argsArray) {
        final var protectedResource = Lists.newArrayList(argsArray).stream()
            .filter(o -> o instanceof ProtectedResource).collect(Collectors.toList());

        if (protectedResource.size() > 1) {
            throw new IllegalStateException("More than one protected resource passed");
        } else if (protectedResource.isEmpty()) {
            throw new IllegalStateException("Passed args do not extend protected resource");
        }

        return (ProtectedResource) protectedResource.get(0);
    }
}
