package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.domain.ExternalPlatform
import dev.lewischan.weatherbot.domain.User
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Component
import unwrap
import java.sql.ResultSet
import java.util.*

@Component
class UserRepositoryImpl(val jdbcClient: JdbcClient) : UserRepository {

    override fun findByExternalUserId(
        externalPlatform: ExternalPlatform,
        externalUserId: UUID
    ): User? {
        return jdbcClient.sql(FIND_USER_BY_EXTERNAL_PLATFORM_ID)
            .param("externalPlatform", externalPlatform.name)
            .param("externalUserId", externalUserId)
            .query(userMapper())
            .optional()
            .unwrap()
    }

    override fun createUser(externalPlatform: ExternalPlatform, externalUserId: UUID): User {
        val userId = jdbcClient.sql(CREATE_USER)
            .param("externalPlatform", externalPlatform.name)
            .param("externalUserId", externalUserId)
            .query(Long::class.java)
            .single()

        return jdbcClient.sql(FIND_USER_BY_ID)
            .param("id", userId)
            .query(userMapper())
            .single()
    }

    companion object {
        const val FIND_USER_BY_EXTERNAL_PLATFORM_ID = "SELECT * FROM users WHERE external_platform = :externalPlatform AND external_user_id = :externalUserId"
        const val CREATE_USER = "INSERT INTO users (external_platform, external_user_id) VALUES (:externalPlatform, :externalUserId) RETURNING id"
        const val FIND_USER_BY_ID = "SELECT * FROM users WHERE id = :id"

        fun userMapper(): (rs: ResultSet, _: Int) -> User {
            return { rs: ResultSet, _: Int -> User(
                rs.getLong("id"),
                enumValueOf<ExternalPlatform>(rs.getString("external_platform")),
                rs.getObject("external_user_id", UUID::class.java)
            ) }
        }
    }
}