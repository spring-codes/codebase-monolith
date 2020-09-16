# Gateway Agence

#### create a directory and move into
```mkdir gateway-agence;cd gateway-agence```

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