package fr.polytech.bike.data.model

import android.os.Parcel
import android.os.Parcelable
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.time.LocalDate
import fr.polytech.bike.BR

data class SignUpRequest(
    @SerializedName("lastname") private var _lastname: String,
    @SerializedName("firstname") private var _firstname: String,
    @SerializedName("username") private var _username: String?,
    @SerializedName("password") private var _password: String?,
    @SerializedName("poids") private var _poids: Double,
    @SerializedName("taille") private var _taille: Double,
    @SerializedName("birthdate") private var _birthdate: LocalDate) :
    BaseObservable() {

    var lastname: String
        @Bindable get() = _lastname
        set(value) {
            _lastname = value
            notifyPropertyChanged(BR.lastname)
        }

    var firstname: String
        @Bindable get() = _firstname
        set(value) {
            _firstname = value
            notifyPropertyChanged(BR.firstname)
        }

    var username: String?
        @Bindable get() = _username
        set(value) {
            _username = value
            notifyPropertyChanged(BR.username)
        }

    var password: String?
        @Bindable get() = _password
        set(value) {
            _password = value
            notifyPropertyChanged(BR.password)
        }

    var poids: Double
        @Bindable get() = _poids
        set(value) {
            _poids = value
            notifyPropertyChanged(BR.poids)
        }

    var taille: Double
        @Bindable get() = _taille
        set(value) {
            _taille = value
            notifyPropertyChanged(BR.taille)
        }

    var birthdate: LocalDate
        @Bindable get() = _birthdate
        set(value) {
            _birthdate = value
            notifyPropertyChanged(BR.birthdate)
        }

    fun toSerializable(): SignUpRequestSerializable {
        return SignUpRequestSerializable(this)
    }
}