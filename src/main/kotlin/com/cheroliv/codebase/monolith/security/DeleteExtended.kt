package com.cheroliv.codebase.monolith.security

import reactor.core.publisher.Mono

interface DeleteExtended<T> {
    fun delete(user: T?): Mono<Void?>?
}