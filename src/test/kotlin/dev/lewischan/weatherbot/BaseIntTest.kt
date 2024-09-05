package dev.lewischan.weatherbot

import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.api.TestInstance.Lifecycle
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
abstract class BaseIntTest {
}