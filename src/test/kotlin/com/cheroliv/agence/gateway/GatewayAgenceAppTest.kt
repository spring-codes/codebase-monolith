package com.cheroliv.agence.gateway

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.test.assertNotNull


class GatewayAgenceAppTest {
    companion object {
        @JvmStatic
        private val log by lazy { LoggerFactory.getLogger(GatewayAgenceAppTest::class.java) }
    }

    @Test
    fun testAppHasAGreeting() {
        assertNotNull(GatewayAgenceApp().greeting,
                "app should have a greeting")
    }
}
