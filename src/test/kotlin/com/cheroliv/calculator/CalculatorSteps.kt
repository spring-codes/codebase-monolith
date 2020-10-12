package com.cheroliv.calculator

import io.cucumber.java.en.Given
import io.cucumber.java.en.Then
import io.cucumber.java.en.When
import org.slf4j.LoggerFactory
import kotlin.test.assertEquals


class CalculatorSteps {
    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(CalculatorSteps::class.java)
    }

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