package com.cheroliv.codebase.monolith.security;

import org.hibernate.validator.internal.constraintvalidators.hv.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.ReactiveUserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements ReactiveUserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UserRepository userRepository;

    public DomainUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Mono<UserDetails> findByUsername(final String login) {
        log.debug("Authenticating {}", login);

        if (new EmailValidator().isValid(login, null)) {
            return requireNonNull(userRepository.findOneWithAuthoritiesByEmailIgnoreCase(login))
                    .switchIfEmpty(Mono.error(new UsernameNotFoundException("User with email " + login + " was not found in the database")))
                    .map(user -> createSpringSecurityUser(login, user));
        }

        String lowercaseLogin = login.toLowerCase(Locale.ENGLISH);
        return requireNonNull(userRepository.findOneWithAuthoritiesByLogin(lowercaseLogin))
                .switchIfEmpty(Mono.error(new UsernameNotFoundException("User " + lowercaseLogin + " was not found in the database")))
                .map(user -> createSpringSecurityUser(lowercaseLogin, user));

    }

    private User createSpringSecurityUser(String lowercaseLogin, AuthUser user) {
        if (!user.getActivated()) {
            throw new UserNotActivatedException("User " + lowercaseLogin + " was not activated");
        }
        List<GrantedAuthority> grantedAuthorities = user.getAuthorities().stream()
                .map(authority -> new SimpleGrantedAuthority(authority.getName()))
                .collect(Collectors.toList());
        return new User(requireNonNull(user.getLogin()),
                requireNonNull(user.getPassword()),
                grantedAuthorities);
    }
}