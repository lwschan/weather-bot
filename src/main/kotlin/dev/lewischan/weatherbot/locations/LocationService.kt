package dev.lewischan.weatherbot.locations

interface LocationService {
    fun geocode(addressQuery: String): Location?;

    fun search(addressQuery: String): List<Location>;
}
