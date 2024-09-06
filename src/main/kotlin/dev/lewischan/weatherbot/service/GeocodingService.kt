package dev.lewischan.weatherbot.service

import org.springframework.stereotype.Service

interface GeocodingService {
    fun getLocation();
}
