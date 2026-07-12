package dev.lewischan.weatherbot.core.model

data class Humidity(val value: Int) {
    override fun toString(): String {
        return "$value%"
    }
}
