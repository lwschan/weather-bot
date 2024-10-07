package dev.lewischan.weatherbot.repository

import dev.lewischan.weatherbot.domain.UserDefaultLocation
import dev.lewischan.weatherbot.model.Location
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.stereotype.Component
import unwrap
import java.sql.ResultSet

@Component
class UserDefaultLocationRepositoryImpl(
    val jdbcClient: JdbcClient
) : UserDefaultLocationRepository {

    companion object {
        const val FIND_BY_USER_ID = "SELECT * FROM user_default_locations WHERE user_id = :userId"
        const val SAVE_USER_DEFAULT_LOCATION = "INSERT INTO user_default_locations (user_id, latitude, longitude) " +
                "VALUES (:userId, :latitude, :longitude) " +
                "ON CONFLICT (user_id) DO UPDATE " +
                "SET latitude = :latitude, longitude = :longitude, last_updated_on = NOW()" +
                "RETURNING id"
        const val DELETE_FOR_USER = "DELETE FROM user_default_locations WHERE user_id = :userId"
        const val FIND_BY_ID = "SELECT * FROM user_default_locations WHERE id = :id"

        fun userDefaultLocationMapper(): (rs: ResultSet, _: Int) -> UserDefaultLocation {
            return { rs: ResultSet, _: Int -> UserDefaultLocation(
                rs.getLong("id"),
                rs.getLong("user_id"),
                Location("", "", rs.getDouble("latitude"), rs.getDouble("longitude"))
            ) }
        }
    }

    override fun findByUserId(userId: Long): UserDefaultLocation? {
        return jdbcClient.sql(FIND_BY_USER_ID)
            .param("userId", userId)
            .query(userDefaultLocationMapper())
            .optional()
            .unwrap()
    }

    override fun save(userId: Long, location: Location): UserDefaultLocation {
        val id = jdbcClient.sql(SAVE_USER_DEFAULT_LOCATION)
            .param("userId", userId)
            .param("latitude", location.latitude)
            .param("longitude", location.longitude)
            .query(Long::class.java)
            .single()

        return jdbcClient.sql(FIND_BY_ID)
            .param("id", id)
            .query(userDefaultLocationMapper())
            .single()
    }

    override fun deleteForUser(userId: Long) {
        jdbcClient.sql(DELETE_FOR_USER)
            .param("userId", userId)
            .query()
    }
}