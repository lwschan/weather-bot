package dev.lewischan.weatherbot.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class TemperatureTest : FunSpec({

    context("Celsius should be converted to Fahrenheit correctly") {
        withData(
            30.4 to 86.72,
            15.0 to 59.0,
            15.23 to 59.41
        ) { (celsius, fahrenheit) ->
            Temperature.celsius(celsius).fahrenheit shouldBe fahrenheit
        }
    }

    context("Fahrenheit should be converted to Celsius correctly") {
        withData(
            86.72 to 30.4,
            212.0 to 100.0
        ) { (fahrenheit, celsius) ->
            Temperature.fahrenheit(fahrenheit).celsius shouldBe celsius

        }
    }

})
