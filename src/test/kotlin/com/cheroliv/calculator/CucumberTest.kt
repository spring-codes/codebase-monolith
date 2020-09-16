package com.cheroliv.calculator


import org.junit.runner.RunWith
import io.cucumber.junit.Cucumber
import io.cucumber.junit.CucumberOptions

@RunWith(Cucumber::class)
@CucumberOptions(plugin = ["pretty"])
class CucumberTest {
    // See:
    // https://github.com/cucumber/cucumber-jvm/issues/1149
    // https://github.com/cucumber/cucumber-jvm/tree/master/junit-platform-engine
}