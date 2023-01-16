package fr.polytech.bike.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import fr.polytech.bike.data.LoginRepository
import fr.polytech.bike.data.Result

import fr.polytech.bike.R
import fr.polytech.bike.data.Preferences
import fr.polytech.bike.data.local.LocalDatabase
import fr.polytech.bike.data.model.Utilisateur
import fr.polytech.bike.repository.ApiClient
import kotlinx.coroutines.*

class LoginViewModel(private val loginRepository: LoginRepository, private val preferences: Preferences) : ViewModel() {

    private val uiScope = CoroutineScope(Dispatchers.IO + Job())

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    init {
        _loginForm.value = LoginFormState(null, null, true)
    }

    fun login(username: String, password: String) {
        uiScope.launch {
            if (loginRepository.login(username, password) is Result.Error) {
                _loginResult.postValue(LoginResult(error = R.string.login_failed))
                return@launch
            }
            val utilisateur: Utilisateur = preferences.getString("user")?.let { ApiClient.gson.fromJson(it, Utilisateur::class.java) }!!
            _loginResult.postValue(LoginResult(success = LoggedInUserView(displayName = utilisateur.prenomUtil + " " + utilisateur.nomUtil)))
        }
    }

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    // A placeholder username validation check
    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    // A placeholder password validation check
    private fun isPasswordValid(password: String): Boolean {
        return password.length > 3
    }
}

data class jwtReponseBody(
    val sub: String,
    val iat: Long,
    val user: Utilisateur,
    val exp: Long
)