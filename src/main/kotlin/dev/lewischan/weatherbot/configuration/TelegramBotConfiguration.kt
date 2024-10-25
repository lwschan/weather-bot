package dev.lewischan.weatherbot.configuration

import dev.lewischan.weatherbot.handler.ChosenInlineResultHandler
import dev.lewischan.weatherbot.handler.CommandHandler
import dev.lewischan.weatherbot.handler.InlineQueryHandler
import dev.lewischan.weatherbot.bot.TelegramBot
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(TelegramBotProperties::class)
class TelegramBotConfiguration {

    @Bean
    fun telegramBotService(
        telegramBotProperties: TelegramBotProperties,
        commandHandlers: List<CommandHandler>,
        inlineQueryHandler: InlineQueryHandler,
        chosenInlineResultHandler: ChosenInlineResultHandler
    ): TelegramBot {
        return TelegramBot(
            telegramBotProperties,
            commandHandlers,
            inlineQueryHandler,
            chosenInlineResultHandler
        )
    }
}