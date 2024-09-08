package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.domain.ExternalPlatform
import dev.lewischan.weatherbot.domain.User

interface UserRepository {

    fun findUserByExternalUserId(externalPlatform: ExternalPlatform, externalUserId: Long): User?

    fun createUser(externalPlatform: ExternalPlatform, externalUserId: Long): User
}