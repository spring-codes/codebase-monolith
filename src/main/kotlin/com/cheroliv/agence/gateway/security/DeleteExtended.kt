package com.cheroliv.agence.gateway.security

import reactor.core.publisher.Mono

interface DeleteExtended<T> {
    fun delete(user: T?): Mono<Void?>?
}