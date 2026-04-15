package model.user

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable
import model.Identifiable

@Entity(tableName = "users") // mixed entity and table
@Serializable
data class User(
    @PrimaryKey override val id: String,
    val login: String,
    val passwordHash: ByteArray
) : Identifiable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is User) return false
        return id == other.id && login == other.login && passwordHash.contentEquals(other.passwordHash)
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + login.hashCode()
        result = 31 * result + passwordHash.contentHashCode()
        return result
    }
}