package dev.lewischan.weatherbot.core.location

interface LocationService {
    fun geocode(addressQuery: String): Location?;

    fun search(addressQuery: String): List<Location>;
}
