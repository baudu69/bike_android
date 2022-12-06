package fr.polytech.bike.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.model.SignUpRequest
import java.time.LocalDate

class SignUpViewModel : ViewModel() {
    private val _lastname = MutableLiveData<String>()
    val lastname: MutableLiveData<String> = _lastname

    private val _firstname = MutableLiveData<String>()
    val firstname: MutableLiveData<String> = _firstname

    private val _username = MutableLiveData<String>()
    val username: MutableLiveData<String> = _username

    private val _password = MutableLiveData<String>()
    val password: MutableLiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: MutableLiveData<String> = _confirmPassword

    private val _birthdate = MutableLiveData<String>()
    val birthdate: MutableLiveData<String> = _birthdate

    private val _poids = MutableLiveData<String>()
    val poids: MutableLiveData<String> = _poids

    private val _taille = MutableLiveData<String>()
    val taille: MutableLiveData<String> = _taille

    private val _dataValid = MutableLiveData<Boolean>()
    val dataValid: MutableLiveData<Boolean> = _dataValid

    init {
        _dataValid.value = false
    }

    private fun isPasswordIdentical(): Boolean {
        return password.value == confirmPassword.value
    }

    private fun isLastNameValid(): Boolean {
        return lastname.value?.isNotBlank() ?: false
    }

    private fun isFirstNameValid(): Boolean {
        return firstname.value?.isNotBlank() ?: false
    }

    private fun isEmailValid(): Boolean {
        return username.value?.isNotBlank() ?: false
    }

    private fun isPasswordValid(): Boolean {
        return password.value?.isNotBlank() ?: false
    }

    private fun isUsernameValid(): Boolean {
        return username.value?.isNotBlank() ?: false
    }

    private fun isTailleValid(): Boolean {
        if (taille.value == null || taille.value?.isBlank() == true) {
            return false
        }
        return try {
            taille.value?.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    private fun isPoidsValid(): Boolean {
        if (poids.value == null || poids.value?.isBlank() == true) {
            return false
        }
        return try {
            poids.value?.toDouble()
            true
        } catch (e: NumberFormatException) {
            false
        }
    }

    fun validData(): Boolean? {
        _dataValid.value = isPasswordIdentical() && isLastNameValid() && isFirstNameValid() && isEmailValid() && isPasswordValid() && isTailleValid() && isPoidsValid() && isUsernameValid()
        return _dataValid.value
    }

    fun getSignupRequest(): SignUpRequest {
        return SignUpRequest(
            lastname.value ?: "",
            firstname.value ?: "",
            username.value ?: "",
            password.value,
            poids.value?.toDouble() ?: 0.0,
            taille.value?.toDouble() ?: 0.0,
            birthdate.value?.let { LocalDate.parse(it) } ?: LocalDate.now()
        )
    }


}