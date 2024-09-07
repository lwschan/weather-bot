package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.domain.ExternalPlatform
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.security.SecureRandom
import java.time.Instant
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserRepositoryIntTest @Autowired constructor(
    private val userRepository: UserRepository
) : BaseIntTest() {

    @Test
    fun createUserShouldCreateUserSuccessfully() {
        val externalUserId = SecureRandom().nextLong(0, 1000000000)
        val user = userRepository.createUser(
            ExternalPlatform.TELEGRAM, externalUserId, Instant.now()
        )
        assertNotNull(user.id)
        assertEquals(user.externalPlatform, ExternalPlatform.TELEGRAM)
        assertEquals(user.externalUserId, externalUserId)
        assertNotNull(user.createdOn)
    }

    @Test
    fun whenExistsFindUserByExternalUserIdShouldReturnUser() {
        val externalUserId = SecureRandom().nextLong(0, 1000000000)
        val createdUser = userRepository.createUser(
            ExternalPlatform.TELEGRAM, externalUserId, Instant.now()
        )
        val user = userRepository.findUserByExternalUserId(ExternalPlatform.TELEGRAM, externalUserId)
        assertNotNull(user)
        assertEquals(createdUser.id, user.id)
        assertEquals(createdUser.externalPlatform, ExternalPlatform.TELEGRAM)
        assertEquals(createdUser.externalUserId, user.externalUserId)
        assertEquals(createdUser.createdOn, user.createdOn)
    }

}