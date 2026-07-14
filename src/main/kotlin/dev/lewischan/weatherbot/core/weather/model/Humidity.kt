package dev.lewischan.weatherbot.core.weather.model

data class Humidity(val value: Int) {
    override fun toString(): String {
        return "$value%"
    }
}
