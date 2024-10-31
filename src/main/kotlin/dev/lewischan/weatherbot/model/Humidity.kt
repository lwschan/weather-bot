package dev.lewischan.weatherbot.model

data class Humidity(val value: Int) {
    override fun toString(): String {
        return "$value%"
    }
}