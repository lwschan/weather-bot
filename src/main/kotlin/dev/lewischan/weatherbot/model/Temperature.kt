package dev.lewischan.weatherbot.model

class Temperature private constructor(
    private val celsius: Double,
    private val fahrenheit: Double,
) {
    override fun toString(): String {
        return "$celsius°C / $fahrenheit°F"
    }

    companion object {
        fun celsius(value: Double): Temperature = Temperature(value, (9/5) * value + 32)
        fun fahrenheit(value: Double): Temperature = Temperature(value * 1.8 + 32, value)
    }
}