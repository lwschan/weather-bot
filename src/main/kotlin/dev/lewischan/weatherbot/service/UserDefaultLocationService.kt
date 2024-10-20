package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.domain.UserDefaultLocation
import dev.lewischan.weatherbot.model.Location
import dev.lewischan.weatherbot.repository.UserDefaultLocationRepository
import org.springframework.stereotype.Service

@Service
class UserDefaultLocationService(
    private val userDefaultLocationRepository: UserDefaultLocationRepository
) {

    fun findByUserId(userId: Long): UserDefaultLocation? = userDefaultLocationRepository.findByUserId(userId)

    fun save(userId: Long, location: Location): UserDefaultLocation = userDefaultLocationRepository.save(userId, location)

    fun deleteForUser(userId: Long) = userDefaultLocationRepository.deleteForUser(userId)

}