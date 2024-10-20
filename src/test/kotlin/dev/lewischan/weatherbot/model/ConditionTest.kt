package dev.lewischan.weatherbot.model

import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe

class ConditionTest : FunSpec({

    context("wmoCode should be mapped to the right condition") {
        withData(
            Pair(Pair(0, true), Condition.SUNNY),
            Pair(Pair(0, false), Condition.CLEAR),
            Pair(Pair(1, true), Condition.MAINLY_SUNNY),
            Pair(Pair(1, false), Condition.MAINLY_CLEAR),
            Pair(Pair(2, true), Condition.PARTLY_CLOUDY),
            Pair(Pair(2, false), Condition.PARTLY_CLOUDY),
            Pair(Pair(3, true), Condition.CLOUDY),
            Pair(Pair(3, false), Condition.CLOUDY),
            Pair(Pair(45, true), Condition.FOGGY),
            Pair(Pair(45, false), Condition.FOGGY),
            Pair(Pair(48, true), Condition.RIME_FOG),
            Pair(Pair(48, false), Condition.RIME_FOG),
            Pair(Pair(51, true), Condition.LIGHT_DRIZZLE),
            Pair(Pair(51, false), Condition.LIGHT_DRIZZLE),
            Pair(Pair(53, true), Condition.DRIZZLE),
            Pair(Pair(53, false), Condition.DRIZZLE),
            Pair(Pair(55, true), Condition.HEAVY_DRIZZLE),
            Pair(Pair(55, false), Condition.HEAVY_DRIZZLE),
            Pair(Pair(56, true), Condition.LIGHT_FREEZING_DRIZZLE),
            Pair(Pair(56, false), Condition.LIGHT_FREEZING_DRIZZLE),
            Pair(Pair(57, true), Condition.FREEZING_DRIZZLE),
            Pair(Pair(57, false), Condition.FREEZING_DRIZZLE),
            Pair(Pair(61, true), Condition.LIGHT_RAIN),
            Pair(Pair(61, false), Condition.LIGHT_RAIN),
            Pair(Pair(63, true), Condition.RAIN),
            Pair(Pair(63, false), Condition.RAIN),
            Pair(Pair(65, true), Condition.HEAVY_RAIN),
            Pair(Pair(65, false), Condition.HEAVY_RAIN),
            Pair(Pair(66, true), Condition.LIGHT_FREEZING_RAIN),
            Pair(Pair(66, false), Condition.LIGHT_FREEZING_RAIN),
            Pair(Pair(67, true), Condition.FREEZING_RAIN),
            Pair(Pair(67, false), Condition.FREEZING_RAIN),
            Pair(Pair(71, true), Condition.LIGHT_SNOW),
            Pair(Pair(71, false), Condition.LIGHT_SNOW),
            Pair(Pair(73, true), Condition.SNOW),
            Pair(Pair(73, false), Condition.SNOW),
            Pair(Pair(75, true), Condition.HEAVY_SNOW),
            Pair(Pair(75, false), Condition.HEAVY_SNOW),
            Pair(Pair(77, true), Condition.SNOW_GRAINS),
            Pair(Pair(77, false), Condition.SNOW_GRAINS),
            Pair(Pair(80, true), Condition.LIGHT_SHOWERS),
            Pair(Pair(80, false), Condition.LIGHT_SHOWERS),
            Pair(Pair(81, true), Condition.SHOWERS),
            Pair(Pair(81, false), Condition.SHOWERS),
            Pair(Pair(82, true), Condition.HEAVY_SHOWERS),
            Pair(Pair(82, false), Condition.HEAVY_SHOWERS),
            Pair(Pair(85, true), Condition.LIGHT_SNOW_SHOWERS),
            Pair(Pair(85, false), Condition.LIGHT_SNOW_SHOWERS),
            Pair(Pair(86, true), Condition.SNOW_SHOWERS),
            Pair(Pair(86, false), Condition.SNOW_SHOWERS),
            Pair(Pair(95, true), Condition.THUNDERSTORM),
            Pair(Pair(95, false), Condition.THUNDERSTORM),
            Pair(Pair(96, true), Condition.LIGHT_THUNDERSTORM_WITH_HAIL),
            Pair(Pair(96, false), Condition.LIGHT_THUNDERSTORM_WITH_HAIL),
            Pair(Pair(99, true), Condition.THUNDERSTORM_WITH_HAIL),
            Pair(Pair(99, false), Condition.THUNDERSTORM_WITH_HAIL)
        ) { (wmoCodeAndIsDay, expectedCondition) ->
            val actual = Condition.fromWmoCodeAndIsDay(wmoCodeAndIsDay.first, wmoCodeAndIsDay.second)
            actual shouldBe expectedCondition
        }
    }
})