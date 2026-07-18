package dev.lewischan.weatherbot.core

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import reactor.core.publisher.Hooks

@SpringBootApplication(scanBasePackages = ["dev.lewischan.weatherbot"])
class WeatherBotApplication

fun main(args: Array<String>) {
    Hooks.enableAutomaticContextPropagation()
    runApplication<WeatherBotApplication>(*args)
}
