package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.domain.ExternalPlatform
import dev.lewischan.weatherbot.domain.User
import dev.lewischan.weatherbot.error.UserAlreadyExistsException
import dev.lewischan.weatherbot.repository.UserRepository
import org.springframework.dao.DuplicateKeyException
import java.util.UUID

abstract class UserService<T>(
    private val userRepository: UserRepository,
) {
    protected abstract val externalPlatform: ExternalPlatform

    fun createUser(externalUserId: T): User {
        val externalUserUuid = convertExternalUserId(externalUserId)
        try {
            return userRepository.createUser(externalPlatform, externalUserUuid)
        } catch (exception: DuplicateKeyException) {
            throw UserAlreadyExistsException(externalPlatform, externalUserId.toString(), exception)
        }
    }

    fun findByExternalUserId(externalUserId: T): User? {
        val externalUserUuid = convertExternalUserId(externalUserId)
        return userRepository.findByExternalUserId(externalPlatform, externalUserUuid)
    }

    protected abstract fun convertExternalUserId(externalUserId: T): UUID
}