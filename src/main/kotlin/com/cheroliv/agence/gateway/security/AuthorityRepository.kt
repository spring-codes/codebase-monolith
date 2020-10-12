package com.cheroliv.agence.gateway.security

import com.cheroliv.agence.gateway.security.Authority
import org.springframework.data.r2dbc.repository.R2dbcRepository

/**
 * Spring Data R2DBC repository for the [Authority] entity.
 */
interface AuthorityRepository : R2dbcRepository<Authority?, String?>
