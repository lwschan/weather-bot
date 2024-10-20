package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.domain.ExternalPlatform
import dev.lewischan.weatherbot.domain.User
import java.util.*

interface UserRepository {

    fun findByExternalUserId(externalPlatform: ExternalPlatform, externalUserId: UUID): User?

    fun createUser(externalPlatform: ExternalPlatform, externalUserId: UUID): User
}