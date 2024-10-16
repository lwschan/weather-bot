package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.model.Location

interface LocationService {
    fun geocode(addressQuery: String): Location?;

    fun search(addressQuery: String): List<Location>;
}
