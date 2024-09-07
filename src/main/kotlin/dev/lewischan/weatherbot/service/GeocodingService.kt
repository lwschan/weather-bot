package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.model.Location

interface GeocodingService {
    fun getLocation(addressQuery: String): Location?;
}
