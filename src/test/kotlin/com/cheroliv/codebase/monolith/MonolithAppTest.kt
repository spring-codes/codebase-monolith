package com.cheroliv.codebase.monolith

import org.junit.jupiter.api.Test
import org.slf4j.LoggerFactory
import kotlin.test.assertNotNull

class MonolithAppTest {
    companion object {
        @JvmStatic
        private val log by lazy { LoggerFactory.getLogger(MonolithAppTest::class.java) }
    }

    @Test
    fun testAppHasAGreeting() {
        assertNotNull(MonolithApp().greeting,
                "app should have a greeting")
    }
}