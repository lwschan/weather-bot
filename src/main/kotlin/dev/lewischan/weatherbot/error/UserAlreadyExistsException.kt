package dev.lewischan.weatherbot.error

import dev.lewischan.weatherbot.domain.ExternalPlatform

class UserAlreadyExistsException(
    externalPlatform: ExternalPlatform,
    externalUserId: String,
    error: Exception
) : Exception("${externalPlatform.name} user with id $externalUserId already exists.", error) {
}