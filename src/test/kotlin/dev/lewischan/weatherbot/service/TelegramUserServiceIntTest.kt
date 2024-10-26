package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.BaseIntTest
import dev.lewischan.weatherbot.domain.ExternalPlatform
import dev.lewischan.weatherbot.error.UserAlreadyExistsException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.longs.shouldBeGreaterThan
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import java.security.SecureRandom

class TelegramUserServiceIntTest(
    private val telegramUserService: TelegramUserService
) : BaseIntTest({

    test("createUser should create a new user") {
        val externalUserId = SecureRandom().nextLong(0, Long.MAX_VALUE)
        val user = telegramUserService.createUser(externalUserId)
        user.id shouldBeGreaterThan 0
        user.externalPlatform shouldBe ExternalPlatform.TELEGRAM
    }

    context("when user already exists") {
        val externalUserId = SecureRandom().nextLong(0, Long.MAX_VALUE)
        val user = telegramUserService.createUser(externalUserId)
        user.id shouldBeGreaterThan 0

        test("createUser should fail with UserAlreadyExistsException") {
            shouldThrow<UserAlreadyExistsException> {
                telegramUserService.createUser(externalUserId)
            }
        }

        test("findByExternalUserId should return User") {
            val foundUser = telegramUserService.findByExternalUserId(externalUserId)
            foundUser shouldNotBe null
            foundUser!!.id shouldBe user.id
            foundUser.externalPlatform shouldBe user.externalPlatform
            foundUser.externalUserId shouldBe user.externalUserId
        }
    }





})
