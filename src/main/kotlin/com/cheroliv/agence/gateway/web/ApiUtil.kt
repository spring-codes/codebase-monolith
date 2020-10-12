package com.cheroliv.agence.gateway.web

import org.springframework.core.io.buffer.DefaultDataBufferFactory
import org.springframework.web.server.ServerWebExchange
import reactor.core.publisher.Mono
import java.nio.charset.StandardCharsets

object ApiUtil {
    fun getExampleResponse(exchange: ServerWebExchange, example: String): Mono<Void> {
        return exchange.response.writeWith(Mono.just(DefaultDataBufferFactory().wrap(example.toByteArray(StandardCharsets.UTF_8))))
    }
}