package dev.lewischan.weatherbot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class WeatherBotApplication

fun main(args: Array<String>) {
    runApplication<WeatherBotApplication>(*args)
}
