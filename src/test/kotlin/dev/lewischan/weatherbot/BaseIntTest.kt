package dev.lewischan.weatherbot

import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(classes = [WeatherBotApplication::class])
@ActiveProfiles("test")
abstract class BaseIntTest(tests: FunSpec.() -> Unit = {}) : FunSpec(tests)