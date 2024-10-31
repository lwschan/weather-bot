package dev.lewischan.weatherbot.controller

import com.github.kotlintelegrambot.entities.Update
import dev.lewischan.weatherbot.bot.TelegramBot
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/telegram-bot")
class TelegramBotController(
    val telegramBotProperties: TelegramBotProperties,
    val telegramBot: TelegramBot
) {

    @PostMapping("/{botApiToken}")
    suspend fun webhook(
        @PathVariable botApiToken: String,
        @RequestBody update: Update
    ): ResponseEntity<Unit> {
        if (botApiToken != telegramBotProperties.apiToken) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }

        telegramBot.processUpdate(update)
        return ResponseEntity.ok().build()
    }

}