package dev.lewischan.weatherbot.helper

import org.springframework.stereotype.Component
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.UUID

@Component
class UuidGenerator {

    fun v5(namespace: UUID, value: String): UUID {
        val md: MessageDigest
        try {
            md = MessageDigest.getInstance("SHA-1")
        } catch (ex: NoSuchAlgorithmException) {
            throw InternalError("SHA-1 not supported", ex)
        }
        md.update(toBytes(namespace))
        md.update(value.toByteArray())
        val bytes = md.digest()
        bytes[6] = ((bytes[6].toInt() and 0x0F) or 0x50).toByte() /* clear version; set to version 5 */
        bytes[8] = ((bytes[8].toInt() and 0x3F) or 0x80).toByte() /* clear variant; set to IETF variant */
        return fromBytes(bytes)
    }

    private fun fromBytes(data: ByteArray): UUID {
        // Based on the private UUID(bytes[]) constructor
        assert(data.size >= 16)
        var mostSignificantBits = 0L
        var leastSignificantBits = 0L
        for (i in 0..7) {
            mostSignificantBits = mostSignificantBits shl 8 or (data[i].toLong() and 0xff)
        }
        for (i in 8..15) {
            leastSignificantBits = leastSignificantBits shl 8 or (data[i].toLong() and 0xff)
        }
        return UUID(mostSignificantBits, leastSignificantBits)
    }

    private fun toBytes(uuid: UUID): ByteArray {
        // inverted logic of fromBytes()
        val out = ByteArray(16)
        val mostSignificantBits = uuid.mostSignificantBits
        val leastSignificantBits = uuid.leastSignificantBits
        for (i in 0..7) {
            out[i] = (mostSignificantBits shr (7 - i) * 8 and 0xff).toByte()
        }
        for (i in 8..15) {
            out[i] = (leastSignificantBits shr (15 - i) * 8 and 0xff).toByte()
        }
        return out
    }

}