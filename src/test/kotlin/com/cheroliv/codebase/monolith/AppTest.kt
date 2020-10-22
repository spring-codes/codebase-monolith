package com.cheroliv.codebase.monolith

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.test.assertNotNull

class AppTest {
    companion object {
        @JvmStatic
        private val log by lazy { LoggerFactory.getLogger(AppTest::class.java) }
    }

    @Test
    fun testAppHasAGreeting() {
        assertNotNull(App().greeting,
                "app should have a greeting")
    }
}