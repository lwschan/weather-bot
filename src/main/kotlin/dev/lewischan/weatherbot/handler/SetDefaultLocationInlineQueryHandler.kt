package dev.lewischan.weatherbot.handler

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.InlineQuery
import com.github.kotlintelegrambot.entities.inlinequeryresults.InlineQueryResult
import com.github.kotlintelegrambot.entities.inlinequeryresults.InputMessageContent
import dev.lewischan.weatherbot.service.LocationService
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class SetDefaultLocationInlineQueryHandler(
    private val locationService: LocationService
) : InlineQueryHandler() {

    override fun handleInlineQuery(
        bot: Bot,
        inlineQuery: InlineQuery
    ) {
        logger.info("Searching locations for inline query: ${inlineQuery.query}")
        val query = inlineQuery.query
        val locations = locationService.search(query)
        logger.info(locations.map { it.toString() }.joinToString(", "))

        val queryResponse = locations.map {
            InlineQueryResult.Article(
                id = UUID.randomUUID().toString(),
                title = it.name,
                description = it.formattedAddress,
                thumbWidth = 0,
                thumbHeight = 0,
                hideUrl = true,
                inputMessageContent = InputMessageContent.Location(
                    latitude = it.latitude.toFloat(),
                    longitude = it.longitude.toFloat()
                )
            )
        }
        bot.answerInlineQuery(inlineQuery.id, queryResponse)
    }
}