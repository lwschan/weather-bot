package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.model.Location
import io.kotest.matchers.shouldBe
import java.security.SecureRandom

class UserDefaultLocationServiceIntTest(
    private val userService: TelegramUserService,
    private val userDefaultLocationService: UserDefaultLocationService
) : BaseIntTest({

    val random = SecureRandom()

    context("when a default location is created") {
        val externalUserId = random.nextLong(1, Long.MAX_VALUE)
        val user = userService.createUser(externalUserId)
        val location = Location(
            "Tokyo Tower",
            "4 Chome-2-8 Shibakoen, Minato City, Tokyo 105-0011, Japan",
            35.6585848,
            139.742858
        )
        val savedDefaultLocation = userDefaultLocationService.save(user.id, location)

        test("the properties should be saved correctly for the user and location") {
            savedDefaultLocation.userId shouldBe user.id
            savedDefaultLocation.location.name shouldBe location.name
            savedDefaultLocation.location.formattedAddress shouldBe location.formattedAddress
            savedDefaultLocation.location.latitude shouldBe location.latitude
            savedDefaultLocation.location.longitude shouldBe location.longitude
        }

        test("should update default location for the user") {
            val newLocation = Location(
                "London Eye",
                "Riverside Building, County Hall, London SE1 7PB, United Kingdom",
                51.5031896,
                -0.1243901
            )
            val updatedDefaultLocation = userDefaultLocationService.save(user.id, newLocation)
            updatedDefaultLocation.userId shouldBe user.id
            updatedDefaultLocation.location.name shouldBe newLocation.name
            updatedDefaultLocation.location.formattedAddress shouldBe newLocation.formattedAddress
            updatedDefaultLocation.location.latitude shouldBe newLocation.latitude
            updatedDefaultLocation.location.longitude shouldBe newLocation.longitude
        }

        test("delete should clear the default location for the user") {
            userDefaultLocationService.deleteForUser(user.id)
            userDefaultLocationService.findByUserId(user.id) shouldBe null
        }
    }

})
