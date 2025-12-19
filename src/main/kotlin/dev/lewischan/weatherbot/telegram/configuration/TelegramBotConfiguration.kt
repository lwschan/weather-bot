package dev.lewischan.weatherbot.telegram.configuration

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.webhook
import dev.lewischan.weatherbot.telegram.TelegramBot
import dev.lewischan.weatherbot.telegram.TelegramBotProvider
import dev.lewischan.weatherbot.telegram.command.Command
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(TelegramBotProperties::class)
class TelegramBotConfiguration {

    @Bean
    fun telegramBot(
        telegramBotProperties: TelegramBotProperties,
        bot: Bot,
        commandHandlers: List<Command>
    ): TelegramBot {
        TelegramBotProvider.set(bot)

        return TelegramBot(
            telegramBotProperties,
            bot,
            commandHandlers
        )
    }

    @Bean
    @ConditionalOnMissingBean
    fun bot(
        telegramBotProperties: TelegramBotProperties,
        commandHandlers: List<Command>
    ): Bot {
        return bot {
            token = telegramBotProperties.apiToken
            dispatch {
                commandHandlers.forEach {
                    command(it.command) {
                        it.execute(message)
                    }
                }
            }
            if (telegramBotProperties.useWebhook && telegramBotProperties.serverHostname != null) {
                webhook {
                    url = "https://${telegramBotProperties.serverHostname}/telegram/${telegramBotProperties.apiToken}"
                    secretToken = telegramBotProperties.webhookSecretToken
                }
            }
        }
    }
}