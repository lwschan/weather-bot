package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.UseBaseIntTest
import dev.lewischan.weatherbot.domain.ExternalPlatform
import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.util.*

@UseBaseIntTest
class UserRepositoryIntTest(
    private val userRepository: UserRepository
) : FunSpec({

    test("create user should create user successfully") {
        val externalUserId = UUID.randomUUID()
        val user = userRepository.createUser(
            ExternalPlatform.TELEGRAM, externalUserId
        )
        user.id shouldBeGreaterThan 0
        user.externalPlatform shouldBe ExternalPlatform.TELEGRAM
        user.externalUserId shouldBe externalUserId
    }

    test("when exists findUserByExternalUserId should return user") {
        val externalUserId = UUID.randomUUID()
        val createdUser = userRepository.createUser(
            ExternalPlatform.TELEGRAM, externalUserId
        )
        val user = userRepository.findByExternalUserId(ExternalPlatform.TELEGRAM, externalUserId)
        user shouldNotBe null
        user!!.id shouldBe createdUser.id
        user.externalPlatform shouldBe createdUser.externalPlatform
        user.externalUserId shouldBe createdUser.externalUserId
    }

    test("when not exist findUserByExternalUserId should return null") {
        val userId = UUID.randomUUID()
        val user = userRepository.findByExternalUserId(ExternalPlatform.TELEGRAM, userId)
        user shouldBe null
    }

})
