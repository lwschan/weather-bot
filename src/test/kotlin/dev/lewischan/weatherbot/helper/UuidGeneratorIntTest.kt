package dev.lewischan.weatherbot.helper

import dev.lewischan.weatherbot.helper.UuidGenerator.Namespace
import io.kotest.core.spec.style.FunSpec
import io.kotest.datatest.withData
import io.kotest.matchers.shouldBe
import java.util.*

class UuidGeneratorIntTest : FunSpec({

    data class UuidTestData(
        val namespace: Namespace,
        val givenValue: String,
        val expectedValue: UUID
    )

    val uuidGenerator: UuidGenerator = UuidGenerator()

    context("UuidGenerator.v5 should return deterministic UUID given the same namespace and value") {
        withData(
            UuidTestData(
                Namespace.TELEGRAM,
                "129847128",
                UUID.fromString("1bf95d21-9526-5522-913f-eac96db24953")
            ),
            UuidTestData(
                Namespace.TELEGRAM,
                "89174194",
                UUID.fromString("42926b0b-febf-5bcb-bbea-f16008c1bc8e")
            )
        ) { (namespace, value, expected) ->
            val uuid1 = uuidGenerator.v5(namespace, value)
            val uuid2 = uuidGenerator.v5(namespace, value)
            uuid1 shouldBe expected
            uuid2 shouldBe expected
        }
    }
})