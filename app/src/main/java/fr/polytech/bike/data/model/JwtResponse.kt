package fr.polytech.bike.data.model

import androidx.annotation.Keep
import androidx.room.*
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.ZoneOffset

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
@Keep
@Entity(tableName = "jwt_response")
data class JwtResponse(
    val jwt: String
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L

    override fun toString(): String {
        return "JwtResponse(jwt='$jwt')"
    }
}

@Keep
@Entity(tableName = "utilisateur")
data class Utilisateur(var nomUtil: String, var prenomUtil: String, var dateNaissance: LocalDate, var taille: Double, var poids: Double, val login: String) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Long = 0L

    override fun toString(): String {
        return "Utilisateur(nomUtil='$nomUtil', prenomUtil='$prenomUtil')"
    }
}

class Converters {
    @TypeConverter
    fun fromTimestamp(value: Long?): LocalDateTime? {
        return value?.let { LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneOffset.UTC) }
    }

    @TypeConverter
    fun dateTimeToTimestamp(date: LocalDateTime?): Long? {
        return date?.atZone(ZoneOffset.UTC)?.toInstant()?.toEpochMilli()
    }

    @TypeConverter
    fun dateToTimestamp(date: LocalDate?): Long? {
        return date?.toEpochDay()
    }

    @TypeConverter
    fun fromTimestampDate(value: Long?): LocalDate? {
        return value?.let { LocalDate.ofEpochDay(value) }
    }
}