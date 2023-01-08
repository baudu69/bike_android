package fr.polytech.bike.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.Keep
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.room.*
import com.google.gson.annotations.SerializedName
import fr.polytech.bike.BR
import fr.polytech.bike.repository.ApiClient
import java.time.*

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
data class Utilisateur(
    @ColumnInfo(name = "nomUtil") @SerializedName("nomUtil") private var _nomUtil: String = "",
    @ColumnInfo(name = "prenomUtil") @SerializedName("prenomUtil") private var _prenomUtil: String = "",
    @ColumnInfo(name = "dateNaissance") @SerializedName("dateNaissance") private var _dateNaissance: LocalDate = LocalDate.now(),
    @ColumnInfo(name = "taille") @SerializedName("taille") private var _taille: Double = 0.0,
    @ColumnInfo(name = "poids") @SerializedName("poids") private var _poids: Double = 0.0,
    @PrimaryKey val login: String
    ): BaseObservable(), Parcelable {

    var nomUtil: String
        @Bindable get() = _nomUtil
        set(value) {
            _nomUtil = value
            notifyPropertyChanged(BR.nomUtil)
        }

    var prenomUtil: String
        @Bindable get() = _prenomUtil
        set(value) {
            _prenomUtil = value
            notifyPropertyChanged(BR.prenomUtil)
        }

    var dateNaissance: LocalDate
        @Bindable get() = _dateNaissance
        set(value) {
            _dateNaissance = value
            notifyPropertyChanged(BR.dateNaissance)
        }

    var taille: Double
        @Bindable get() = _taille
        set(value) {
            _taille = value
            notifyPropertyChanged(BR.taille)
        }

    var poids: Double
        @Bindable get() = _poids
        set(value) {
            _poids = value
            notifyPropertyChanged(BR.poids)
        }

    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readSerializable() as LocalDate,
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString()!!
    ) {
    }

    fun toSignUpRequest(): SignUpRequest {
        return SignUpRequest(
            _lastname = nomUtil,
            _firstname = prenomUtil,
            _birthdate = _dateNaissance,
            _password = "",
            _poids = _poids,
            _taille = _taille,
            _username = login
        )
    }

    override fun toString(): String {
        return "Utilisateur(nomUtil='$nomUtil', prenomUtil='$prenomUtil')"
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(_nomUtil)
        parcel.writeString(_prenomUtil)
        parcel.writeSerializable(_dateNaissance)
        parcel.writeDouble(_taille)
        parcel.writeDouble(_poids)
        parcel.writeString(login)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Utilisateur> {
        override fun createFromParcel(parcel: Parcel): Utilisateur {
            return Utilisateur(parcel)
        }

        override fun newArray(size: Int): Array<Utilisateur?> {
            return arrayOfNulls(size)
        }
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

    @TypeConverter
    fun fromListEtape(value: List<Etape>?): String? {
        return value?.let { ApiClient.gson.toJson(value) }
    }

    @TypeConverter
    fun toListEtape(value: String?): List<Etape>? {
        return value?.let { ApiClient.gson.fromJson(value, Array<Etape>::class.java).toList() }
    }

    @TypeConverter
    fun fromLocalTime(value: LocalTime?): Long? {
        return value?.toNanoOfDay()
    }

    @TypeConverter
    fun toLocalTime(value: Long?): LocalTime? {
        return value?.let { LocalTime.ofNanoOfDay(value) }
    }
}