package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.domain.ExternalPlatform
import dev.lewischan.weatherbot.model.Location
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import java.security.SecureRandom

class UserDefaultLocationRepositoryIntTest @Autowired constructor(
    private val userDefaultLocationRepository: UserDefaultLocationRepository,
    private val userRepository: UserRepository
) : BaseIntTest() {

    @Test
    fun whenExistsFindByUserIdShouldReturnRecord() {
        val externalUserId = SecureRandom().nextLong(0, 1000000000)
        val user = userRepository.createUser(
            ExternalPlatform.TELEGRAM, externalUserId
        )
        val savedUserDefaultLocation = userDefaultLocationRepository.save(user.id, Location(53.990129, -0.9140249))
        val foundUserDefaultLocation = userDefaultLocationRepository.findByUserId(user.id)

        assertNotNull(foundUserDefaultLocation)
        assertEquals(savedUserDefaultLocation.id, foundUserDefaultLocation!!.id)
        assertEquals(savedUserDefaultLocation.userId, foundUserDefaultLocation.userId)
        assertEquals(53.990129, foundUserDefaultLocation.location.latitude)
        assertEquals(-0.9140249, foundUserDefaultLocation.location.longitude)
    }

    @Test
    fun whenNotExistFindByUserIdShouldReturnNull() {
        val userId = SecureRandom().nextLong(0, 1000000000)
        val userDefaultLocation = userDefaultLocationRepository.findByUserId(userId)
        assertNull(userDefaultLocation)
    }

    @Test
    fun saveShouldSaveUserDefaultLocationSuccessfully() {
        val externalUserId = SecureRandom().nextLong(0, 1000000000)
        val user = userRepository.createUser(
            ExternalPlatform.TELEGRAM, externalUserId
        )
        val userDefaultLocation = userDefaultLocationRepository.save(user.id, Location(53.990129, -0.9140249))
        assertNotNull(userDefaultLocation.id)
        assertEquals(userDefaultLocation.userId, user.id)
        assertEquals(53.990129, userDefaultLocation.location.latitude)
        assertEquals(-0.9140249, userDefaultLocation.location.longitude)
    }

    @Test
    fun whenRecordExistsSaveShouldUpsert() {
        val externalUserId = SecureRandom().nextLong(0, 1000000000)
        val user = userRepository.createUser(
            ExternalPlatform.TELEGRAM, externalUserId
        )
        val userDefaultLocation = userDefaultLocationRepository.save(user.id, Location(53.990129, -0.9140249))
        assertNotNull(userDefaultLocation.id)
        assertEquals(userDefaultLocation.userId, user.id)
        assertEquals(53.990129, userDefaultLocation.location.latitude)
        assertEquals(-0.9140249, userDefaultLocation.location.longitude)

        val updatedUserDefaultLocation = userDefaultLocationRepository.save(user.id, Location(35.7015871, 139.6885977))
        assertNotNull(updatedUserDefaultLocation.id)
        assertEquals(userDefaultLocation.id, updatedUserDefaultLocation.id)
        assertEquals(userDefaultLocation.userId, updatedUserDefaultLocation.userId)
        assertEquals(35.7015871, updatedUserDefaultLocation.location.latitude)
        assertEquals(139.6885977, updatedUserDefaultLocation.location.longitude)
    }
}