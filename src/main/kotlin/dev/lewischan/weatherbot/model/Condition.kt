package dev.lewischan.weatherbot.model

enum class Condition(
    val wmoCode: Int,
    val isDay: Boolean?,
    val value: String
) {
    UNKNOWN(-1, "Unknown"),
    SUNNY(0, true, "Sunny"),
    CLEAR(0, false, "Clear"),
    MAINLY_SUNNY(1, true, "Mainly Sunny"),
    MAINLY_CLEAR(1, false, "Mainly Clear"),
    PARTLY_CLOUDY(2, "Partly Cloudy"),
    CLOUDY(3, "Cloudy"),
    FOGGY(45, "Foggy"),
    RIME_FOG(48, "Rime Fog"),
    LIGHT_DRIZZLE(51, "Light Drizzle"),
    DRIZZLE(53, "Drizzle"),
    HEAVY_DRIZZLE(55, "Heavy Drizzle"),
    LIGHT_FREEZING_DRIZZLE(56, "Light Freezing Drizzle"),
    FREEZING_DRIZZLE(57, "Freezing Drizzle"),
    LIGHT_RAIN(61, "Light Rain"),
    RAIN(63, "Rain"),
    HEAVY_RAIN(65, "Heavy Rain"),
    LIGHT_FREEZING_RAIN(66, "Light Freezing Rain"),
    FREEZING_RAIN(67, "Freezing Rain"),
    LIGHT_SNOW(71, "Light Snow"),
    SNOW(73, "Snow"),
    HEAVY_SNOW(75, "Heavy Snow"),
    SNOW_GRAINS(77, "Snow Grains"),
    LIGHT_SHOWERS(80, "Light Showers"),
    SHOWERS(81, "Showers"),
    HEAVY_SHOWERS(82, "Heavy Showers"),
    LIGHT_SNOW_SHOWERS(85, "Light Snow Showers"),
    SNOW_SHOWERS(86, "Snow Showers"),
    THUNDERSTORM(95, "Thunderstorm"),
    LIGHT_THUNDERSTORM_WITH_HAIL(96, "Light Thunderstorm with Hail"),
    THUNDERSTORM_WITH_HAIL(99, "Thunderstorm with Hail");

    constructor(wmoCode: Int, value: String) : this(wmoCode, null, value)

    companion object {
        fun fromWmoCodeAndIsDay(wmoCode: Int, isDay: Boolean): Condition {
            val conditionsMatched = entries.filter { it.wmoCode == wmoCode }
            return conditionsMatched.firstOrNull { it.isDay == null || it.isDay == isDay } ?: UNKNOWN
        }
    }

}