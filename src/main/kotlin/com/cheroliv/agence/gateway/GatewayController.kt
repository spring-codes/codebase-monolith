package com.cheroliv.agence.gateway

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/")
class GatewayController {
    companion object {
        @JvmStatic
        @GetMapping
        fun index(): String {
            return "Greetings from Agence Gateway!"
        }
    }
}