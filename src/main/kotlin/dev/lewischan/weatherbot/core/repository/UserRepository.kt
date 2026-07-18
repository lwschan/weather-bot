package dev.lewischan.weatherbot.core.repository

import dev.lewischan.weatherbot.core.domain.ExternalPlatform
import dev.lewischan.weatherbot.core.domain.User
import java.util.*

interface UserRepository {

    fun findByExternalUserId(externalPlatform: ExternalPlatform, externalUserId: UUID): User?

    fun createUser(externalPlatform: ExternalPlatform, externalUserId: UUID): User
}
