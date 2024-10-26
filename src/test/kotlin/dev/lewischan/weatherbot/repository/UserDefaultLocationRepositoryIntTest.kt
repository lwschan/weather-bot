package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.domain.ExternalPlatform
import dev.lewischan.weatherbot.model.Location
import io.kotest.matchers.equality.shouldBeEqualUsingFields
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.springframework.beans.factory.annotation.Autowired
import java.security.SecureRandom
import java.util.*

class UserDefaultLocationRepositoryIntTest @Autowired constructor(
    private val userDefaultLocationRepository: UserDefaultLocationRepository,
    private val userRepository: UserRepository
) : BaseIntTest({

    context("when user default location is created") {
        val externalUserId = UUID.randomUUID()
        val user = userRepository.createUser(
            ExternalPlatform.TELEGRAM, externalUserId
        )
        val defaultLocation = Location(
            "Jewel Changi Airport",
            "78 Airport Boulevard, Singapore 819666",
            1.3602148,
            103.9871849
        )
        val savedUserDefaultLocation = userDefaultLocationRepository.save(user.id, defaultLocation)

        test("fields should be correct") {
            savedUserDefaultLocation.userId shouldBe user.id
            savedUserDefaultLocation.location shouldBeEqualUsingFields defaultLocation
        }

        test("findByUserId should return the default location") {
            val foundUserDefaultLocation = userDefaultLocationRepository.findByUserId(user.id)

            foundUserDefaultLocation shouldNotBe null
            foundUserDefaultLocation!!.id shouldBe savedUserDefaultLocation.id
            foundUserDefaultLocation.userId shouldBe savedUserDefaultLocation.userId
            foundUserDefaultLocation.location shouldBeEqualUsingFields defaultLocation
        }

        test("save should upsert the record") {
            val newDefaultLocation = Location(
                "Merlion Park",
                "1 Fullerton Rd, Singapore 049213",
                1.2867503,
                103.8518123
            )
            val updatedUserDefaultLocation = userDefaultLocationRepository.save(user.id, newDefaultLocation)
            updatedUserDefaultLocation.id shouldBe savedUserDefaultLocation.id
            updatedUserDefaultLocation.userId shouldBe savedUserDefaultLocation.userId
            updatedUserDefaultLocation.location shouldBeEqualUsingFields newDefaultLocation
        }

        test("delete should delete the record") {
            userDefaultLocationRepository.deleteForUser(user.id)
            userDefaultLocationRepository.findByUserId(user.id) shouldBe null
        }
    }

    test("when not exists, findByUserId should return null") {
        val userId = SecureRandom().nextLong(0, Long.MAX_VALUE)
        val userDefaultLocation = userDefaultLocationRepository.findByUserId(userId)
        userDefaultLocation shouldBe null
    }

})