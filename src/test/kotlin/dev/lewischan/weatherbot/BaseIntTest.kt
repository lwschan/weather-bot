package dev.lewischan.weatherbot

import io.kotest.core.spec.style.FunSpec
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.webtestclient.autoconfigure.AutoConfigureWebTestClient
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    classes = [WeatherBotApplication::class],
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT
)
@AutoConfigureWebTestClient
@ActiveProfiles("test")
abstract class BaseIntTest(tests: FunSpec.() -> Unit = {}) : FunSpec(tests)