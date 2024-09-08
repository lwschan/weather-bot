package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.domain.ExternalPlatform
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.security.SecureRandom
import kotlin.test.assertEquals
import kotlin.test.assertNotNull
import kotlin.test.assertNull

class UserRepositoryIntTest @Autowired constructor(
    private val userRepository: UserRepository
) : BaseIntTest() {

    @Test
    fun createUserShouldCreateUserSuccessfully() {
        val externalUserId = SecureRandom().nextLong(0, 1000000000)
        val user = userRepository.createUser(
            ExternalPlatform.TELEGRAM, externalUserId
        )
        assertNotNull(user.id)
        assertEquals(user.externalPlatform, ExternalPlatform.TELEGRAM)
        assertEquals(user.externalUserId, externalUserId)
    }

    @Test
    fun whenExistsFindUserByExternalUserIdShouldReturnUser() {
        val externalUserId = SecureRandom().nextLong(0, 1000000000)
        val createdUser = userRepository.createUser(
            ExternalPlatform.TELEGRAM, externalUserId
        )
        val user = userRepository.findUserByExternalUserId(ExternalPlatform.TELEGRAM, externalUserId)
        assertNotNull(user)
        assertEquals(createdUser.id, user.id)
        assertEquals(createdUser.externalPlatform, ExternalPlatform.TELEGRAM)
        assertEquals(createdUser.externalUserId, user.externalUserId)
    }

    @Test
    fun whenNotExistFindUserByExternalUserIdShouldReturnNull() {
        val userId = SecureRandom().nextLong(0, 1000000000)
        val user = userRepository.findUserByExternalUserId(ExternalPlatform.TELEGRAM, userId)
        assertNull(user)
    }
}