package dev.lewischan.weatherbot.error

import com.github.kotlintelegrambot.types.TelegramBotResult

class TelegramBotSendMessageException(
    message: String,
    exception: Exception?
) : Exception(message, exception) {

    constructor(message: String) : this(message, null)

    companion object {
        fun create(telegramBotErrorResult: TelegramBotResult.Error): TelegramBotSendMessageException {
            return when (telegramBotErrorResult) {
                is TelegramBotResult.Error.HttpError -> TelegramBotSendMessageException(
                    "HttpError with httpCode ${telegramBotErrorResult.httpCode}, description ${telegramBotErrorResult.description}."
                )
                is TelegramBotResult.Error.InvalidResponse -> TelegramBotSendMessageException(
                    "InvalidResponse with httpCode ${telegramBotErrorResult.httpCode}, httpStatusMessage ${telegramBotErrorResult.httpStatusMessage}, body ${telegramBotErrorResult.body}"
                )
                is TelegramBotResult.Error.TelegramApi -> TelegramBotSendMessageException(
                    "TelegramApi with errorCode ${telegramBotErrorResult.errorCode}, description ${telegramBotErrorResult.description}"
                )
                is TelegramBotResult.Error.Unknown -> TelegramBotSendMessageException(
                    "Unknown", telegramBotErrorResult.exception
                )
            }
        }
    }
}