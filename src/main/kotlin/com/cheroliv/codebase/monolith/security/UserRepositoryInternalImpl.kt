package com.cheroliv.codebase.monolith.security

import io.r2dbc.spi.Row
import io.r2dbc.spi.RowMetadata
import org.springframework.data.domain.Pageable
import org.springframework.data.r2dbc.core.DatabaseClient
import org.springframework.data.r2dbc.core.ReactiveDataAccessStrategy
import org.springframework.data.relational.core.query.Criteria
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import reactor.util.function.Tuple2
import reactor.util.function.Tuples
import java.util.*
import java.util.stream.Collectors

class UserRepositoryInternalImpl(
        private val db: DatabaseClient,
        private val dataAccessStrategy: ReactiveDataAccessStrategy) : UserRepositoryInternal {
    override fun findOneWithAuthoritiesByLogin(login: String?): Mono<AuthUser?>? {
        return findOneWithAuthoritiesBy("login", login)
    }

    override fun findOneWithAuthoritiesByEmailIgnoreCase(email: String?): Mono<AuthUser?>? {
        return findOneWithAuthoritiesBy("email", email!!.toLowerCase())
    }

    private fun findOneWithAuthoritiesBy(fieldName: String, fieldValue: Any?): Mono<AuthUser?> {
        return db.execute("SELECT * FROM user u LEFT JOIN user_authority ua ON u.id=ua.user_id WHERE u.$fieldName = :$fieldName")
                .bind(fieldName, fieldValue!!)
                .map { row: Row, metadata: RowMetadata? ->
                    Tuples.of(
                            dataAccessStrategy.getRowMapper(AuthUser::class.java).apply(row, metadata),
                            Optional.ofNullable(row.get("authority_name", String::class.java))
                    )
                }
                .all()
                .collectList()
                .filter { l: List<Tuple2<AuthUser, Optional<String>>> -> !l.isEmpty() }
                .map { l: List<Tuple2<AuthUser, Optional<String>>> ->
                    val user = l[0].t1
                    user.authorities = l.stream()
                            .filter { t: Tuple2<AuthUser, Optional<String>> -> t.t2.isPresent }
                            .map { t: Tuple2<AuthUser, Optional<String>> ->
                                val authority = Authority()
                                authority.name = t.t2.get()
                                authority
                            }
                            .collect(Collectors.toSet())
                    user
                }
    }

    override fun findAllByLoginNot(pageable: Pageable?, login: String?): Flux<AuthUser?>? {
        return db.select().from(AuthUser::class.java)
                .matching(Criteria.where("login").not(login!!))
                .page(pageable!!)
                .`as`(AuthUser::class.java)
                .all()
    }

    override fun delete(user: AuthUser?): Mono<Void?>? {
        return db.execute("DELETE FROM user_authority WHERE user_id = :userId")
                .bind("userId", Objects.requireNonNull(user!!.id)!!)
                .then()
                .then(db.delete()
                        .from(AuthUser::class.java)
                        .matching(Criteria.where("id").`is`(user.id!!))
                        .then()
                )
    }
}