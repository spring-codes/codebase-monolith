package com.cheroliv.codebase.monolith.security;

import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

/**
 * Spring Data R2DBC repository for the {@link  AuthUser} entity.
 */
@Repository
public interface UserRepository extends R2dbcRepository<AuthUser, Long>, UserRepositoryInternal {

    @Query("SELECT * FROM user WHERE activation_key = :activationKey")
    Mono<AuthUser> findOneByActivationKey(String activationKey);

    @Query("SELECT * FROM user WHERE activated = false AND activation_key IS NOT NULL AND created_date < :dateTime")
    Flux<AuthUser> findAllByActivatedIsFalseAndActivationKeyIsNotNullAndCreatedDateBefore(LocalDateTime dateTime);

    @Query("SELECT * FROM user WHERE reset_key = :resetKey")
    Mono<AuthUser> findOneByResetKey(String resetKey);

    @Query("SELECT * FROM user WHERE LOWER(email) = LOWER(:email)")
    Mono<AuthUser> findOneByEmailIgnoreCase(String email);

    @Query("SELECT * FROM user WHERE login = :login")
    Mono<AuthUser> findOneByLogin(String login);

    @Query("SELECT COUNT(DISTINCT id) FROM user WHERE login != :anonymousUser")
    Mono<Long> countAllByLoginNot(String anonymousUser);

    @Query("INSERT INTO user_authority VALUES(:userId, :authority)")
    Mono<Void> saveUserAuthority(Long userId, String authority);

    @Query("DELETE FROM user_authority")
    Mono<Void> deleteAllUserAuthorities();
}

