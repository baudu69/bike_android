package fr.polytech.bike.ui.signup

import android.util.Log
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.model.SignUpRequest
import fr.polytech.bike.data.model.SignUpRequestSerializable
import fr.polytech.bike.repository.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate

class SignUpViewModel : ViewModel() {

    private val _navigateToConnexion = MutableLiveData<Boolean>()
    val navigateToConnexion: LiveData<Boolean>
        get() = _navigateToConnexion


    private var viewModelJob = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + viewModelJob)
    private val authRepository = ApiClient.authRepository

    private val _signUpRequest: MutableLiveData<SignUpRequest> = MutableLiveData()
    val signUpRequest: LiveData<SignUpRequest>
        get() = _signUpRequest

    val confirmPassword: MutableLiveData<String> = MutableLiveData()
    val birthDate: MutableLiveData<String> = MutableLiveData(LocalDate.now().toString())

    init {
        _signUpRequest.value = SignUpRequest("", "", "", "", 0.0, 0.0, LocalDate.now())
    }

    private fun isPasswordIdentical(): Boolean {
        return _signUpRequest.value?.password == confirmPassword.value
    }

    private fun isLastNameValid(): Boolean {
        return _signUpRequest.value?.lastname?.isNotBlank() ?: false
    }

    private fun isFirstNameValid(): Boolean {
        return _signUpRequest.value?.firstname?.isNotBlank() ?: false
    }

    private fun isEmailValid(): Boolean {
        return _signUpRequest.value?.username?.isNotBlank() ?: false
    }

    private fun isPasswordValid(): Boolean {
        return _signUpRequest.value?.password?.isNotBlank() ?: false
    }

    private fun isUsernameValid(): Boolean {
        return _signUpRequest.value?.username?.isNotBlank() ?: false
    }

    private fun isTailleValid(): Boolean {
        return _signUpRequest.value?.taille != null
    }

    private fun isPoidsValid(): Boolean {
        return _signUpRequest.value?.poids != null
    }

    private fun isBirthDateValid(): Boolean {
        return try {
            LocalDate.parse(birthDate.value)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun validData(): Boolean {
        return isPasswordIdentical() && isLastNameValid() && isFirstNameValid() && isEmailValid() && isPasswordValid() && isTailleValid() && isPoidsValid() && isUsernameValid()
    }

    fun onValid() {
        if (!validData()) {
            Log.d("SignUpViewModel", "Invalid data")
        }
        uiScope.launch {
            val obj = signUpRequest.value!!
            obj.birthdate = LocalDate.parse(birthDate.value)
            val request = authRepository.register(obj.toSerializable())
            Log.d("SignUpViewModel", "Request: $request")
            if (!request.isSuccessful) {
                Log.e("SignUpActivity", "onCreate error: ${request.errorBody()}")
                return@launch
            }
            _navigateToConnexion.postValue(true)
            Log.d("SignUpActivity", "onCreate: ${request.body()}")
        }

    }


}