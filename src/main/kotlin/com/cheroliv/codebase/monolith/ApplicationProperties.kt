package com.cheroliv.codebase.monolith

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.stereotype.Component

@Component
@ConfigurationProperties(
        prefix = "application",
        ignoreUnknownFields = false)
class ApplicationProperties {
    companion object Data
}

interface ApplicationDefaults{
    interface Data
}

class ApplicationConstants

