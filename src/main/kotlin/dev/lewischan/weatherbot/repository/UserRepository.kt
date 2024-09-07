package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.domain.ExternalPlatform
import dev.lewischan.weatherbot.domain.User
import java.time.Instant

interface UserRepository {
    fun findUserByExternalUserId(externalPlatform: ExternalPlatform, externalUserId: Long): User?

    fun createUser(externalPlatform: ExternalPlatform, externalUserId: Long, createdOn: Instant): User
}