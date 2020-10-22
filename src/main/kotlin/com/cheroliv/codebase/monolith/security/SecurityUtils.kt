package com.cheroliv.codebase.monolith.security

import org.springframework.security.core.Authentication
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.context.ReactiveSecurityContextHolder
import org.springframework.security.core.context.SecurityContext
import org.springframework.security.core.userdetails.UserDetails
import reactor.core.publisher.Mono

/**
 * Utility class for Spring Security.
 */
object SecurityUtils {
    /**
     * Get the login of the current user.
     *
     * @return the login of the current user.
     */
    val currentUserLogin: Mono<String?>
        get() = ReactiveSecurityContextHolder.getContext()
                .map { obj: SecurityContext -> obj.authentication }
                .flatMap { authentication: Authentication? -> Mono.justOrEmpty(extractPrincipal(authentication)) }

    private fun extractPrincipal(authentication: Authentication?): String? {
        if (authentication == null) {
            return null
        } else if (authentication.principal is UserDetails) {
            val springSecurityUser = authentication.principal as UserDetails
            return springSecurityUser.username
        } else if (authentication.principal is String) {
            return authentication.principal as String
        }
        return null
    }

    /**
     * Get the JWT of the current user.
     *
     * @return the JWT of the current user.
     */
    val currentUserJWT: Mono<String?>
        get() = ReactiveSecurityContextHolder.getContext()
                .map { obj: SecurityContext -> obj.authentication }
                .filter { authentication: Authentication -> authentication.credentials is String }
                .map { authentication: Authentication -> authentication.credentials as String }

    /**
     * Check if a user is authenticated.
     *
     * @return true if the user is authenticated, false otherwise.
     */
    val isAuthenticated: Mono<Boolean>
        get() = ReactiveSecurityContextHolder.getContext()
                .map { obj: SecurityContext -> obj.authentication }
                .map { obj: Authentication -> obj.authorities }
                .map { authorities: Collection<GrantedAuthority> ->
                    authorities.stream()
                            .map { obj: GrantedAuthority -> obj.authority }
                            .noneMatch { anObject: String? -> AuthoritiesConstants.ANONYMOUS.equals(anObject) }
                }

    /**
     * If the current user has a specific authority (security role).
     *
     *
     * The name of this method comes from the `isUserInRole()` method in the Servlet API.
     *
     * @param authority the authority to check.
     * @return true if the current user has the authority, false otherwise.
     */
    fun isCurrentUserInRole(authority: String): Mono<Boolean> {
        return ReactiveSecurityContextHolder.getContext()
                .map { obj: SecurityContext -> obj.authentication }
                .map { obj: Authentication -> obj.authorities }
                .map { authorities: Collection<GrantedAuthority> ->
                    authorities.stream()
                            .map { obj: GrantedAuthority -> obj.authority }
                            .anyMatch { anObject: String? -> authority.equals(anObject) }
                }
    }
}