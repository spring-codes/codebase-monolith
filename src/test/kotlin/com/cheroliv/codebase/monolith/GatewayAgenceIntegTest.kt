package com.cheroliv.codebase.monolith

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class GatewayAgenceIntegTest {
    companion object {
        @JvmStatic
        private val log by lazy { LoggerFactory.getLogger(GatewayAgenceIntegTest::class.java) }
    }

    @Test
    fun contextLoads() {
        log.info("contextLoads()...")
    }
}