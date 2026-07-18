package dev.lewischan.weatherbot.core.error

import dev.lewischan.weatherbot.core.domain.ExternalPlatform

class UserAlreadyExistsException(
    externalPlatform: ExternalPlatform,
    externalUserId: String,
    error: Exception
) : Exception("${externalPlatform.name} user with id $externalUserId already exists.", error)
