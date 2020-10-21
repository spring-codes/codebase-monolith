package com.cheroliv.agence.gateway.web

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/")
class GatewayController {
    companion object {
        @JvmStatic
        @GetMapping
        fun index(): String = "Greetings from Agence Gateway!"
    }
}