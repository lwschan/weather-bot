package dev.lewischan.weatherbot.controller

import com.github.kotlintelegrambot.entities.Update
import dev.lewischan.weatherbot.configuration.TelegramBotProperties
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/telegram-bot")
class TelegramBotController(
    val telegramBotProperties: TelegramBotProperties
) {

    @PostMapping("/{botApiToken}")
    fun webhook(
        @PathVariable botApiToken: String,
        @RequestBody update: Update
    ): ResponseEntity<Void> {
        return ResponseEntity.ok().build()
    }

}