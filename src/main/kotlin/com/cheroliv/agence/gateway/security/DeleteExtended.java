package com.cheroliv.agence.gateway.security;

import reactor.core.publisher.Mono;

public interface DeleteExtended<T> {
    Mono<Void> delete(T user);
}
