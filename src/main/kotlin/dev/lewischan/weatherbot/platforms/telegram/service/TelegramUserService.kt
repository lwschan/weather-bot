package dev.lewischan.weatherbot.platforms.telegram.service

import dev.lewischan.weatherbot.core.domain.ExternalPlatform
import dev.lewischan.weatherbot.core.helper.UuidGenerator
import dev.lewischan.weatherbot.core.repository.UserRepository
import dev.lewischan.weatherbot.core.service.UserService
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
