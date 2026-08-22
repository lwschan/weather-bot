package dev.lewischan.weatherbot.platforms.telegram.extension

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.LinkPreviewOptions
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyParameters
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.types.TelegramBotResult
import dev.lewischan.weatherbot.platforms.telegram.error.TelegramBotSendMessageException

@Suppress("kotlin:S107")
fun Bot.replyMessage(
    chatId: ChatId,
    text: String,
    parseMode: ParseMode? = null,
    linkPreviewOptions: LinkPreviewOptions? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    replyParameters: ReplyParameters? = null,
    replyMarkup: ReplyMarkup? = null,
    messageThreadId: Long? = null,
): TelegramBotResult<Message> {
    return sendMessage(
        chatId = chatId,
        text = text,
        parseMode = parseMode,
        linkPreviewOptions = linkPreviewOptions,
        disableNotification = disableNotification,
        protectContent = protectContent,
        replyParameters = replyParameters,
        replyMarkup = replyMarkup,
        messageThreadId = messageThreadId
    ).onError {
        throw TelegramBotSendMessageException.create(it)
    }
}
