import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version ("1.4.10")
    kotlin("plugin.jpa") version ("1.3.72")
    kotlin("plugin.spring") version ("1.4.10")
    id("io.spring.dependency-management") version ("1.0.10.RELEASE")
    id("org.springframework.boot") version ("2.3.4.RELEASE")
    id("com.google.cloud.tools.jib") version ("2.6.0")
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

repositories {
    mavenCentral()
    jcenter()
}

group = (properties["project_artifact_group"] as String?)!!
version = (properties["project_version"] as String?)!!

java.sourceCompatibility = JavaVersion.VERSION_15

defaultTasks(properties["default_task_name"] as String?)

springBoot {
    mainClassName = properties["main_class"] as String?
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
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    // Use the Kotlin test library.
    testImplementation("org.jetbrains.kotlin:kotlin-test")
    // Use the Kotlin JUnit integration.
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit") {
        exclude("junit", "junit")
    }
    //testImplementation("org.junit.jupiter:junit-jupiter-engine:$junitJupiterVersion")
    testImplementation("io.cucumber:cucumber-java:${properties["cucumber_version"]}")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:${properties["cucumber_version"]}")
    testImplementation("io.cucumber:cucumber-junit:${properties["cucumber_version"]}")
    // Junit
    testImplementation("org.junit.jupiter:junit-jupiter:${properties["junit_jupiter_version"]}")
    testImplementation("org.junit.vintage:junit-vintage-engine:${properties["junit_jupiter_version"]}")
    // add coroutines support
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor")
    // add spring boot support
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    // Spring Metrics
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    // Spring web
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot", "spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    // Reactor
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    testImplementation("io.projectreactor:reactor-test")

    // Spring boot test
    testImplementation("org.springframework.boot:spring-boot-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude("org.junit.vintage", "junit-vintage-engine")
    }
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    // Jackson
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    // Bean Validation
    implementation("org.springframework.boot:spring-boot-starter-validation")
    // JPA
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    // R2DBC
    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")

    //Security
    implementation("org.springframework.security:spring-security-web")
    implementation("org.springframework.security:spring-security-data")
    implementation("org.springframework.security:spring-security-config")
    testImplementation("org.springframework.security:spring-security-test") {
        exclude("junit", "junit")
    }
    //JWT
    implementation("org.zalando:problem-spring-webflux:${properties["problem_spring_webflux_version"]}")
    implementation("io.jsonwebtoken:jjwt-api:${properties["jsonwebtoken_version"]}")
    runtimeOnly("io.jsonwebtoken:jjwt-impl:${properties["jsonwebtoken_version"]}")
    implementation("io.jsonwebtoken:jjwt-jackson:${properties["jsonwebtoken_version"]}")
    // misc
    implementation("org.apache.commons:commons-lang3:${properties["commons_lang3_version"]}")
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
        freeCompilerArgs = listOf(properties["kotlin_compiler_options"]) as List<String>
        jvmTarget = properties["jvm_target"] as String
    }
}

jib {
    from {
        this.image = properties["app_docker_base_image"] as String?
    }
    to {
        this.image = properties["docker_hub_image_repo"] as String?
    }
}