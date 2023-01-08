package fr.polytech.bike.ui.signup

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.model.SignUpRequest
import fr.polytech.bike.repository.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.time.LocalDate

class SignUpViewModel : ViewModel() {

    val SignUpFormState = SignUpFormState()

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

    fun checkLastName() {
        val text = signUpRequest.value!!.lastname
        if (text.isEmpty()) {
            SignUpFormState.lastnameError = "Le nom ne peut pas être vide"
        } else {
            SignUpFormState.lastnameError = null
        }
    }

    fun checkFirstName() {
        val text = signUpRequest.value!!.firstname
        if (text.isEmpty()) {
            SignUpFormState.firstnameError = "Le prénom ne peut pas être vide"
        } else {
            SignUpFormState.firstnameError = null
        }
    }

    fun checkTaille() {
        val text = signUpRequest.value!!.taille
        if (text <= 0) {
            SignUpFormState.tailleError = "La taille doit être supérieure à 0"
        } else {
            SignUpFormState.tailleError = null
        }
    }

    fun checkPoids() {
        val text = signUpRequest.value!!.poids
        if (text <= 0) {
            SignUpFormState.poidsError = "Le poids doit être supérieur à 0"
        } else {
            SignUpFormState.poidsError = null
        }
    }

    fun checkBirthDate() {
        val date = signUpRequest.value!!.birthdate
        if (date.isAfter(LocalDate.now())) {
            SignUpFormState.birthdateError = "La date de naissance ne peut pas être dans le futur"
            return
        }
        SignUpFormState.birthdateError = null
    }

    fun checkLogin() {
        val text = signUpRequest.value!!.username!!
        if (text.isEmpty()) {
            SignUpFormState.usernameError = "Le login ne peut pas être vide"
        } else {
            SignUpFormState.usernameError = null
        }
    }

    fun checkPassword() {
        val text = signUpRequest.value!!.password!!
        if (text.isEmpty()) {
            SignUpFormState.passwordError = "Le mot de passe ne peut pas être vide"
            return
        }
        if (text.length < 4) {
            SignUpFormState.passwordError = "Le mot de passe doit contenir au moins 8 caractères"
            return
        }
        SignUpFormState.passwordError = null
    }

    fun checkValidPassword() {
        val text = signUpRequest.value!!.password!!
        val text2 = confirmPassword.value!!
        if (text != text2) {
            SignUpFormState.passwordConfirmError = "Les mots de passe ne correspondent pas"
        } else {
            SignUpFormState.passwordConfirmError = null
        }
    }

    fun onValid() {
        if (SignUpFormState.lastnameError != null || SignUpFormState.firstnameError != null || SignUpFormState.tailleError != null || SignUpFormState.poidsError != null || SignUpFormState.birthdateError != null || SignUpFormState.usernameError != null || SignUpFormState.passwordError != null || SignUpFormState.passwordConfirmError != null) {
            return
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