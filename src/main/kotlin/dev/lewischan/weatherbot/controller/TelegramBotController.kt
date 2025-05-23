package dev.lewischan.weatherbot.controller

import dev.lewischan.weatherbot.bot.TelegramBot
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/telegram")
class TelegramBotController(
    val telegramBotProperties: TelegramBotProperties,
    val telegramBot: TelegramBot
) {

    val logger: Logger = LoggerFactory.getLogger(javaClass)

    @PostMapping("/{botApiToken}")
    suspend fun webhook(
        @PathVariable botApiToken: String,
        @RequestBody update: String
    ): ResponseEntity<Unit> {
        logger.info("Handling webhook request for Telegram on thread ${Thread.currentThread()}")

        if (botApiToken != telegramBotProperties.apiToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        telegramBot.processUpdate(update)
        return ResponseEntity.ok().build()
    }

}