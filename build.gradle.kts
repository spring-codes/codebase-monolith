import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"

    // Apply the application plugin to add support for building a CLI application.
    application
}



repositories {
//    mavenCentral()
    jcenter()
}

dependencies {
    val junitJupiterVersion = "5.7.0"
    val cucumberVersion = "6.7.0"
    val kotlin_version = "1.4.10"

    // Align versions of all Kotlin components
    implementation(platform("org.jetbrains.kotlin:kotlin-bom"))

    // Use the Kotlin JDK 8 standard library.
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")

    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")

    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit") {
        exclude("junit", "junit")
    }

    testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
//    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit:$cucumberVersion")

    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testImplementation("org.junit.vintage:junit-vintage-engine:$junitJupiterVersion")
//    testImplementation("org.jetbrains.kotlin:kotlin-stdlib:$kotlin_version")
}

application {
    // Define the main class for the application.
    mainClassName = "com.cheroliv.agence.gateway.AppKt"
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED, SKIPPED, FAILED)
        showStandardStreams = true
    }
}

//tasks.compileKotlin {
//    val compileParameter = "-parameters"
//    when {
//        kotlinOptions.freeCompilerArgs.isEmpty() -> {
//            kotlinOptions.freeCompilerArgs = listOf(compileParameter)
//        }
//        else -> {
//            kotlinOptions.freeCompilerArgs =
//                    kotlinOptions.freeCompilerArgs.plus(compileParameter)
//        }
//    }
//}