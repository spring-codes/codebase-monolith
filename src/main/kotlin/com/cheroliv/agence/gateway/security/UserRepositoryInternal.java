package com.cheroliv.agence.gateway.security;

import org.springframework.data.domain.Pageable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepositoryInternal extends DeleteExtended<AuthUser> {

    Mono<AuthUser> findOneWithAuthoritiesByLogin(String login);

    Mono<AuthUser> findOneWithAuthoritiesByEmailIgnoreCase(String email);

    Flux<AuthUser> findAllByLoginNot(Pageable pageable, String login);
}
