package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.repository.UserDefaultLocationRepository
import org.springframework.stereotype.Service

@Service
class DefaultLocationService(
    private val userDefaultLocationRepository: UserDefaultLocationRepository,
    private val locationService: LocationService
) {
}