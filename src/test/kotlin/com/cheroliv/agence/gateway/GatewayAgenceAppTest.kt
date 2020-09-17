package com.cheroliv.agence.gateway

import org.junit.jupiter.api.Test
import kotlin.test.assertNotNull

class GatewayAgenceAppTest {
    @Test fun testAppHasAGreeting() {
        assertNotNull(GatewayAgenceApp().greeting,
                "app should have a greeting")
    }
}
