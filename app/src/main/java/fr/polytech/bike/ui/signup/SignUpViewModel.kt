package fr.polytech.bike.ui.signup

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SignUpViewModel : ViewModel() {
    private val _lastname = MutableLiveData<String>()
    val lastname: MutableLiveData<String> = _lastname

    private val _firstname = MutableLiveData<String>()
    val firstname: MutableLiveData<String> = _firstname

    private val _email = MutableLiveData<String>()
    val email: MutableLiveData<String> = _email

    private val _password = MutableLiveData<String>()
    val password: MutableLiveData<String> = _password

    private val _confirmPassword = MutableLiveData<String>()
    val confirmPassword: MutableLiveData<String> = _confirmPassword

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
        return email.value?.isNotBlank() ?: false
    }

    fun validData() {
        _dataValid.value = isPasswordIdentical() && isLastNameValid() && isFirstNameValid() && isEmailValid()
    }


}