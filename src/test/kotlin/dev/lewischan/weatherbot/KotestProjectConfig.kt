package dev.lewischan.weatherbot

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.test.AssertionMode
import io.kotest.engine.concurrency.TestExecutionMode
import io.kotest.extensions.spring.SpringExtension

class KotestProjectConfig : AbstractProjectConfig() {

    override val testExecutionMode = TestExecutionMode.Sequential
    override val assertionMode = AssertionMode.Error

    override val extensions = super.extensions + listOf(
        SpringExtension()
    )
}