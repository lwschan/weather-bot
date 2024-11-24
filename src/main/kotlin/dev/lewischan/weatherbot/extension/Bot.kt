package dev.lewischan.weatherbot.extension

import com.github.kotlintelegrambot.Bot
import com.github.kotlintelegrambot.entities.ChatId
import com.github.kotlintelegrambot.entities.Message
import com.github.kotlintelegrambot.entities.ParseMode
import com.github.kotlintelegrambot.entities.ReplyMarkup
import com.github.kotlintelegrambot.types.TelegramBotResult
import dev.lewischan.weatherbot.error.TelegramBotSendMessageException

@Suppress("kotlin:S107")
fun Bot.replyMessage(
    chatId: ChatId,
    text: String,
    parseMode: ParseMode? = null,
    disableWebPagePreview: Boolean? = null,
    disableNotification: Boolean? = null,
    protectContent: Boolean? = null,
    replyToMessageId: Long? = null,
    allowSendingWithoutReply: Boolean? = null,
    replyMarkup: ReplyMarkup? = null,
    messageThreadId: Long? = null,
): TelegramBotResult<Message> {
    return sendMessage(
        chatId, text, parseMode, disableWebPagePreview, disableNotification,
        protectContent, replyToMessageId, allowSendingWithoutReply, replyMarkup,
        messageThreadId
    ).onError {
        throw TelegramBotSendMessageException.create(it)
    }
}