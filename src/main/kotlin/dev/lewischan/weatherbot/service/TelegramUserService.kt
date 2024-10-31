package dev.lewischan.weatherbot.service

import dev.lewischan.weatherbot.domain.ExternalPlatform
import dev.lewischan.weatherbot.helper.UuidGenerator
import dev.lewischan.weatherbot.repository.UserRepository
import org.springframework.stereotype.Service
import java.util.*

@Service
class TelegramUserService(
    private val uuidGenerator: UuidGenerator,
    userRepository: UserRepository
) : UserService<Long>(
    userRepository
) {
    override val externalPlatform = ExternalPlatform.TELEGRAM

    override fun convertExternalUserId(externalUserId: Long): UUID {
        return uuidGenerator.v5(ExternalPlatform.TELEGRAM.id, externalUserId.toString())
    }
}