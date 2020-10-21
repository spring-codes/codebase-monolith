package com.cheroliv.agence.gateway.security;

import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.core.DatabaseClient;
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuples;

import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;
import static org.springframework.data.r2dbc.query.Criteria.where;

public class UserRepositoryInternalImpl implements UserRepositoryInternal {
    private final DatabaseClient db;
    private final ReactiveDataAccessStrategy dataAccessStrategy;

    public UserRepositoryInternalImpl(DatabaseClient db, ReactiveDataAccessStrategy dataAccessStrategy) {
        this.db = db;
        this.dataAccessStrategy = dataAccessStrategy;
    }

    @Override
    public Mono<AuthUser> findOneWithAuthoritiesByLogin(String login) {
        return findOneWithAuthoritiesBy("login", login);
    }

    @Override
    public Mono<AuthUser> findOneWithAuthoritiesByEmailIgnoreCase(String email) {
        return findOneWithAuthoritiesBy("email", email.toLowerCase());
    }

    private Mono<AuthUser> findOneWithAuthoritiesBy(String fieldName, Object fieldValue) {
        return db.execute("SELECT * FROM user u LEFT JOIN user_authority ua ON u.id=ua.user_id WHERE u." + fieldName + " = :" + fieldName)
                .bind(fieldName, fieldValue)
                .map((row, metadata) ->
                        Tuples.of(
                                dataAccessStrategy.getRowMapper(AuthUser.class).apply(row, metadata),
                                Optional.ofNullable(row.get("authority_name", String.class))
                        )
                )
                .all()
                .collectList()
                .filter(l -> !l.isEmpty())
                .map(l -> {
                    AuthUser user = l.get(0).getT1();
                    user.setAuthorities(
                            l.stream()
                                    .filter(t -> t.getT2().isPresent())
                                    .map(t -> {
                                        Authority authority = new Authority();
                                        authority.setName(t.getT2().get());
                                        return authority;
                                    })
                                    .collect(Collectors.toSet())
                    );
                    return user;
                });
    }

    @Override
    public Flux<AuthUser> findAllByLoginNot(Pageable pageable, String login) {
        return db.select().from(AuthUser.class)
                .matching(where("login").not(login))
                .page(pageable)
                .as(AuthUser.class)
                .all();
    }

    @Override
    public Mono<Void> delete(AuthUser user) {
        return db.execute("DELETE FROM user_authority WHERE user_id = :userId")
                .bind("userId", requireNonNull(user.getId()))
                .then()
                .then(db.delete()
                        .from(AuthUser.class)
                        .matching(where("id").is(user.getId()))
                        .then()
                );
    }

}
