package com.cheroliv.codebase.monolith.web

import org.slf4j.Logger
import org.slf4j.LoggerFactory.getLogger
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/")
class GatewayController {

    companion object {
        @JvmStatic
        private val log: Logger by lazy { getLogger(GatewayController::class.java) }

        @JvmStatic
        @GetMapping
        fun index(): String = "Greetings from Agence Gateway!"
    }
}