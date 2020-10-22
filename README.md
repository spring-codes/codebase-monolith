# Codebase Monolith

#### create a directory and move into
```mkdir codebase-monolith;cd codebase-monolith```

#### initialise git repository
```git init; touch .gitignore```

#### fullfill .gitignore
check .gitignore file

#### initialise a gradle project
```gradle init```

#### add junit5 and cucumber
based on this git repository [mastering-junit5](https://github.com/bonigarcia/mastering-junit5/tree/master/junit5-cucumber "mastering-junit5"),
and at the end the build.gradle should look like this
```
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    // Apply the application plugin to add support
    // for building a CLI application.
    application
}

repositories {
    jcenter()
}

dependencies {
    val junitJupiterVersion = "5.7.0"
    val cucumberVersion = "6.7.0"

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
```

##### create a gherkin feature file
```
mkdir -p  src/main/kotlin/com/cheroliv/calculator
mkdir -p  src/test/kotlin/com/cheroliv/calculator
mkdir -p  src/test/resources/com/cheroliv/calculator
touch src/main/kotlin/com/cheroliv/calculator/Calculator.kt
touch src/test/kotlin/com/cheroliv/calculator/CalculatorSteps.kt
touch src/test/kotlin/com/cheroliv/calculator/CucumberTest.kt
touch src/test/resources/com/cheroliv/calculator/Calculator.feature
touch src/test/resources/junit-platform.properties
```



src/main/kotlin/com/cheroliv/calculator/Calculator.kt

```
package com.cheroliv.calculator

import java.util.*


class Calculator {
    private val stack: Deque<Number?> = LinkedList()

    fun push(arg: Any?) {
        when {
            OPS.contains(arg) -> {
                var `val`: Double? = null
                val y = stack.removeLast()
                val x = when {
                    stack.isEmpty() -> 0
                    else -> stack.removeLast()!!
                }
                when {
                    y != null -> {
                        when (arg) {
                            "-" -> `val` = x.toDouble() - y.toDouble()
                            "+" -> `val` = x.toDouble() + y.toDouble()
                            "*" -> `val` = x.toDouble() * y.toDouble()
                            "/" ->`val` = x.toDouble() / y.toDouble()
                        }
                    }
                }
                push(`val`)
            }
            else -> stack += arg as Number?
        }
    }

    fun value(): Number? {
        return stack.last
    }

    companion object {
        private val OPS = listOf("-", "+", "*", "/")
    }
}
```



src/test/kotlin/com/cheroliv/calculator/CalculatorSteps.kt

```
package com.cheroliv.calculator

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import kotlin.test.assertEquals


class CalculatorSteps {
    private var calc: Calculator? = null

    @Given("^a calculator I just turned on$")
    fun setup() {
        calc = Calculator()
    }

    @When("^I add (\\d+) and (\\d+)$")
    fun add(arg1: Int, arg2: Int) {
        calc!!.push(arg1)
        calc!!.push(arg2)
        calc!!.push("+")
    }

    @When("^I substract (\\d+) to (\\d+)$")
    fun substract(arg1: Int, arg2: Int) {
        calc!!.push(arg1)
        calc!!.push(arg2)
        calc!!.push("-")
    }

    @Then("^the result is (\\d+)$")
    fun the_result_is(expected: Double) {
        assertEquals(expected, calc!!.value())
    }
}
```


src/test/kotlin/com/cheroliv/calculator/CucumberTest.kt
```
package com.cheroliv.calculator

import org.junit.runner.RunWith
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions

@RunWith(Cucumber::class)
@CucumberOptions(plugin = ["pretty"])
class CucumberTest 
```


src/test/resources/junit-platform.properties
```
cucumber.publish.quiet=true
```


src/test/resources/com/cheroliv/calculator/calculator.feature
```
Feature: Basic Arithmetic

  Background: A Calculator
    Given a calculator I just turned on

  Scenario: Addition
    When I add 4 and 5
    Then the result is 9

  Scenario: Substraction
    When I substract 7 to 2
    Then the result is 5

  Scenario Outline: Several additions
    When I add <a> and <b>
    Then the result is <c>

    Examples: Single digits
      | a | b | c  |
      | 1 | 2 | 3  |
      | 3 | 7 | 10 |
```

To play the cucumber tests, run the command :
```
./gradlew clean check
```


the output should look like this :

```
Gradle Test Executor 6 STANDARD_ERROR
    sept. 17, 2020 12:56:04 AM org.junit.platform.launcher.core.LauncherConfigurationParameters fromClasspathResource
    INFO: Loading JUnit Platform configuration parameters from classpath resource [file:/home/cheroliv/src/developer-project/codebase-monolith/build/resources/test/junit-platform.properties].

com.cheroliv.calculator.CucumberTest > Addition STANDARD_OUT

    Scenario: Addition                    # com/cheroliv/calculator/calculator.feature:6
      Given a calculator I just turned on # void com.cheroliv.calculator.CalculatorSteps.setup()
      When I add 4 and 5                  # void com.cheroliv.calculator.CalculatorSteps.add(int,int)
      Then the result is 9                # void com.cheroliv.calculator.CalculatorSteps.the_result_is(double)

com.cheroliv.calculator.CucumberTest > Addition PASSEDberTest

com.cheroliv.calculator.CucumberTest > Substraction STANDARD_OUT

    Scenario: Substraction                # com/cheroliv/calculator/calculator.feature:10
      Given a calculator I just turned on # void com.cheroliv.calculator.CalculatorSteps.setup()
      When I substract 7 to 2             # void com.cheroliv.calculator.CalculatorSteps.substract(int,int)
      Then the result is 5                # void com.cheroliv.calculator.CalculatorSteps.the_result_is(double)

com.cheroliv.calculator.CucumberTest > Substraction PASSED

com.cheroliv.calculator.CucumberTest > Several additions #1 STANDARD_OUT

    Scenario Outline: Several additions   # com/cheroliv/calculator/calculator.feature:20
      Given a calculator I just turned on # void com.cheroliv.calculator.CalculatorSteps.setup()
      When I add 1 and 2                  # void com.cheroliv.calculator.CalculatorSteps.add(int,int)
      Then the result is 3                # void com.cheroliv.calculator.CalculatorSteps.the_result_is(double)

com.cheroliv.calculator.CucumberTest > Several additions #1 PASSED

com.cheroliv.calculator.CucumberTest > Several additions #2 STANDARD_OUT

    Scenario Outline: Several additions   # com/cheroliv/calculator/calculator.feature:21
      Given a calculator I just turned on # void com.cheroliv.calculator.CalculatorSteps.setup()
      When I add 3 and 7                  # void com.cheroliv.calculator.CalculatorSteps.add(int,int)
      Then the result is 10               # void com.cheroliv.calculator.CalculatorSteps.the_result_is(double)

com.cheroliv.calculator.CucumberTest > Several additions #2 PASSED

com.cheroliv.calculator.CucumberTest STANDARD_ERROR
    ┌─────────────────────────────────────────────────────────────────────────────┐
    │ Share your Cucumber Report with your team at https://reports.cucumber.io    │
    │ Activate publishing with one of the following:                              │
    │                                                                             │
    │ src/test/resources/cucumber.properties:    cucumber.publish.enabled=true    │
    │ Environment variable:                      CUCUMBER_PUBLISH_ENABLED=true    │
    │ JUnit:                                     @CucumberOptions(publish = true) │
    │                                                                             │
    │ More information at https://reports.cucumber.io/docs/cucumber-jvm           │
    │                                                                             │
    │ To disable this message, add cucumber.publish.quiet=true to                 │
    │ src/test/resources/cucumber.properties                                      │
    └─────────────────────────────────────────────────────────────────────────────┘

com.cheroliv.agence.gateway.AppTest > testAppHasAGreeting() PASSED

BUILD SUCCESSFUL in 21s
5 actionable tasks: 5 executed

````


#### Add kotlin coroutines support
add support for kotlin coroutines, and a coroutine wrapper for spring reactor.

gradle config, build.gradle.kts :
```
import org.gradle.api.tasks.testing.logging.TestLogEvent.*

plugins {
    id("org.jetbrains.kotlin.jvm") version "1.4.10"
    // Apply the application plugin to add support
    // for building a CLI application.
    application
}

repositories {
    jcenter()
}

dependencies {
    val junitJupiterVersion = "5.7.0"
    val cucumberVersion = "6.7.0"
    val coroutinesVersion = "1.3.9"

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
    // testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumberVersion")
    testImplementation("io.cucumber:cucumber-junit:$cucumberVersion")
    testImplementation("org.junit.jupiter:junit-jupiter:$junitJupiterVersion")
    testImplementation("org.junit.vintage:junit-vintage-engine:$junitJupiterVersion")
    // add coroutines support
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:$coroutinesVersion")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-reactor:$coroutinesVersion")
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
```


#### add spring boot support
add spring spring boot support, after build.gradle.kts should look like this :
```
import org.gradle.api.tasks.testing.logging.TestLogEvent.*
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    kotlin("jvm") version "1.4.10"
    kotlin("plugin.spring") version "1.4.10"
    id("io.spring.dependency-management") version "1.0.10.RELEASE"
    id("org.springframework.boot") version "2.3.3.RELEASE"
}

repositories {
    mavenCentral()
    jcenter()
}

group = "com.cheroliv.saas"
version = "0.0.1"

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

dependencies {
    val junitJupiterVersion = "5.7.0"
    val cucumberVersion = "6.7.0"


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
    implementation("org.springframework.boot:spring-boot-starter-web") {
        exclude("org.springframework.boot","spring-boot-starter-tomcat")
    }
    implementation("org.springframework.boot:spring-boot-starter-undertow")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.boot:spring-boot-starter-test") {
        exclude( "org.junit.vintage",  "junit-vintage-engine")
    }
    developmentOnly("org.springframework.boot:spring-boot-devtools")


//    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("io.projectreactor.kotlin:reactor-kotlin-extensions")
    implementation("org.jetbrains.kotlin:kotlin-reflect")

    implementation("org.springframework.boot:spring-boot-starter-data-r2dbc")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    runtimeOnly("com.h2database:h2")
    runtimeOnly("io.r2dbc:r2dbc-h2")
    runtimeOnly("io.r2dbc:r2dbc-postgresql")
    runtimeOnly("org.postgresql:postgresql")
    annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
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
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "1.8"
    }
}

java.sourceCompatibility = JavaVersion.VERSION_14

springBoot {
    mainClassName = "com.cheroliv.agence.gateway.GatewayAgenceAppKt"
}

defaultTasks( "bootRun")
```

##### after let's add the yaml spring configuration properties file
```
mkdir src/main/resources/config
mkdir src/test/resources/config
touch src/main/resources/config/application.yaml
touch src/test/resources/config/application.yaml
```

##### create the spring boot entry point
let's rename the App class to GatewayAgenceApp
here is how it looks like now :
```
package com.cheroliv.agence.gateway

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class GatewayAgenceApp {
    val greeting: String = "Hello world."
}

fun main(args: Array<String>) {
    runApplication<GatewayAgenceApp>(*args)
}
```

to be able to run the app, just run this command in the terminal:
```
./gradlew bootRun
```



### jib docker support

```import Build_gradle.Constants.appDockerBaseImage
   import Build_gradle.Constants.cucumberVersion
   import Build_gradle.Constants.dockerHubImageRepo
   import Build_gradle.Constants.dockerHubPasswordKey
   import Build_gradle.Constants.dockerHubUsernameKey
   import Build_gradle.Constants.junitJupiterVersion
   import Build_gradle.Constants.jvmTargetVersion
   import Build_gradle.Constants.kotlinCompilerOptions
   import Build_gradle.Constants.kotlinVersion
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
       const val kotlinVersion="1.4.10"
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
   
   java.sourceCompatibility = JavaVersion.VERSION_14
   
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

```

Now we can run tests, with this command in the terminal:
```
./gradlew test bootRun
```