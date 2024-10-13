package dev.lewischan.weatherbot

import io.kotest.core.config.AbstractProjectConfig
import io.kotest.core.extensions.Extension
import io.kotest.extensions.spring.SpringExtension

class TestProjectConfiguration : AbstractProjectConfig() {

    override val parallelism: Int = 3

    override fun extensions(): List<Extension> = super.extensions() + listOf(
        SpringExtension
    )
}