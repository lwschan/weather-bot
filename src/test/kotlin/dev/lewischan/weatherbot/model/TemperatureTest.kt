package dev.lewischan.weatherbot.model

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.DynamicTest
import org.junit.jupiter.api.TestFactory

class TemperatureTest {

    @TestFactory
    fun testCelsiusToFahrenheit() = listOf(
        30.4 to 86.72,
        15.0 to 59.0,
        15.23 to 59.41
    ).map { (celsius, fahrenheit) ->
        DynamicTest.dynamicTest("Celsius $celsius should be Fahrenheit $fahrenheit") {
            assertEquals(fahrenheit, Temperature.celsius(celsius).fahrenheit)
        }
    }

    @TestFactory
    fun testFahrenheitToCelsius() = listOf(
        86.72 to 30.4,
        212.0 to 100.0
    ).map { (fahrenheit, celsius) ->
        DynamicTest.dynamicTest("Fahrenheit $fahrenheit should be Celsius $celsius") {
            assertEquals(celsius, Temperature.fahrenheit(fahrenheit).celsius)
        }
    }
}