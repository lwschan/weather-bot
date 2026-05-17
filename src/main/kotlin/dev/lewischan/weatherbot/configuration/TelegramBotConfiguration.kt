package dev.lewischan.weatherbot.configuration

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.bot
import com.github.kotlintelegrambot.dispatch
import com.github.kotlintelegrambot.dispatcher.command
import com.github.kotlintelegrambot.webhook
import dev.lewischan.weatherbot.handler.CommandHandler
import dev.lewischan.weatherbot.bot.TelegramBot
import dev.lewischan.weatherbot.bot.TelegramBotProvider
import dev.lewischan.weatherbot.infrastructure.ContextPropagatingDispatcher
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.core.PropagationContextElement

@Configuration
@EnableConfigurationProperties(TelegramBotProperties::class)
class TelegramBotConfiguration {

    @Bean
    fun telegramBot(
        telegramBotProperties: TelegramBotProperties,
        bot: Bot,
        commandHandlers: List<CommandHandler>
    ): TelegramBot {
        TelegramBotProvider.set(bot)

        return TelegramBot(
            telegramBotProperties,
            bot,
            commandHandlers
        )
    }

    @Bean(destroyMethod = "shutdown")
    fun contextPropagatingDispatcher(): ContextPropagatingDispatcher {
        return ContextPropagatingDispatcher()
    }

    @Bean
    @ConditionalOnMissingBean
    fun bot(
        telegramBotProperties: TelegramBotProperties,
        commandHandlers: List<CommandHandler>,
        contextPropagatingDispatcher: ContextPropagatingDispatcher
    ): Bot {
        return bot {
            token = telegramBotProperties.apiToken
            coroutineDispatcher = contextPropagatingDispatcher + PropagationContextElement()
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