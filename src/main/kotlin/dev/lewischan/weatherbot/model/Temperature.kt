package dev.lewischan.weatherbot.model

import kotlin.math.round

class Temperature private constructor(
    val celsius: Double,
    val fahrenheit: Double,
) {
    override fun toString(): String {
        return "$celsius°C / $fahrenheit°F"
    }

    companion object {
        fun celsius(value: Double): Temperature = Temperature(
            value,
            ((value * 9.0/5.0) + 32.0).let { round(it * 100) / 100 }
        )

        fun fahrenheit(value: Double): Temperature = Temperature(
            ((value - 32.0) * 5.0/9.0).let { round(it * 100) / 100 },
            value
        )
    }
}