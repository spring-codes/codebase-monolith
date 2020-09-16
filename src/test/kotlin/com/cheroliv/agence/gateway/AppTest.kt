package com.cheroliv.agence.gateway

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class AppTest {
    @Test fun testAppHasAGreeting() {
        assertNotNull(App().greeting,
                "app should have a greeting")
    }
}
