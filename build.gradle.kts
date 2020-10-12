import Build_gradle.Constants.appDockerBaseImage
import Build_gradle.Constants.cucumberVersion
import Build_gradle.Constants.dockerHubImageRepo
import Build_gradle.Constants.dockerHubPasswordKey
import Build_gradle.Constants.dockerHubUsernameKey
import Build_gradle.Constants.jsonwebtokenVersion
import Build_gradle.Constants.junitJupiterVersion
import Build_gradle.Constants.jvmTargetVersion
import Build_gradle.Constants.kotlinCompilerOptions
import Build_gradle.Constants.problemSpringWebfluxVersion
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

object Constants {
    const val projectArtifactGroup = "com.cheroliv.saas"
    const val projectVersion = "0.0.1"
    const val mainClass = "com.cheroliv.agence.gateway.GatewayAgenceAppKt"
    const val defaultTaskName = "bootRun"
    const val jvmTargetVersion = "1.8"
    const val kotlinCompilerOptions = "-Xjsr305=strict"
    const val dockerGradleFileConfigPath = "docker.gradle.kts"
    const val junitJupiterVersion = "5.7.0"
    const val cucumberVersion = "6.7.0"
    const val dockerHubUsernameKey = "hub_docker_com_personal_username"
    const val dockerHubPasswordKey = "hub_docker_com_personal_password"
    const val dockerHubImageRepo = "cheroliv/agence-gateway"
    const val appDockerBaseImage = "adoptopenjdk/openjdk15-openj9:jre-15_36_openj9-0.22.0"
    const val kotlinVersion = "1.4.10"
    const val jsonwebtokenVersion = "0.11.2"
    const val problemSpringWebfluxVersion = "0.26.2"
}

buildscript {
    repositories {
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://repo.spring.io/plugins-release")
        maven("https://repo.spring.io/milestone")
    }
}


plugins {
    kotlin("jvm") version ("1.4.10")
    kotlin("plugin.spring") version ("1.4.10")
    id("io.spring.dependency-management") version ("1.0.10.RELEASE")
    id("org.springframework.boot") version ("2.3.4.RELEASE")
    id("com.google.cloud.tools.jib") version ("2.6.0")
}


repositories {
    mavenCentral()
    jcenter()
}

group = Constants.projectArtifactGroup
version = Constants.projectVersion

java.sourceCompatibility = JavaVersion.VERSION_15

defaultTasks(Constants.defaultTaskName)

springBoot {
    mainClassName = Constants.mainClass
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    //Logger
    implementation("ch.qos.logback:logback-classic")
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
    //testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("io.cucumber:cucumber-java:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit:$cucumberVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testImplementation("org.junit.vintage:junit-vintage-engine:$junitJupiterVersion")
    // add coroutines support
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    // add spring boot support
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.boot:spring-boot-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")
    //runtimeOnly("io.r2dbc:r2dbc-postgresql")
    //runtimeOnly("org.postgresql:postgresql")

    //Security
    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework.security:spring-security-data")
    implementation("org.springframework.security:spring-security-config")
    testImplementation("org.springframework.security:spring-security-test") {
        exclude("junit", "junit")
    }

    implementation("org.zalando:problem-spring-webflux:$problemSpringWebfluxVersion")
    implementation("io.jsonwebtoken:jjwt-api:$jsonwebtokenVersion")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:$jsonwebtokenVersion")
    implementation("io.jsonwebtoken:jjwt-jackson:$jsonwebtokenVersion")
}

tasks.withType<Test> {
    useJUnitPlatform()
    testLogging {
        events = setOf(PASSED, SKIPPED, FAILED)
        showStandardStreams = true
    }
}


tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf(kotlinCompilerOptions)
        jvmTarget = jvmTargetVersion
    }
}

jib {
    from {
        image = appDockerBaseImage
    }
    to {
        image = dockerHubImageRepo
        auth {
            username = project.properties[dockerHubUsernameKey] as String?
            password = project.properties[dockerHubPasswordKey] as String?
        }
    }
}