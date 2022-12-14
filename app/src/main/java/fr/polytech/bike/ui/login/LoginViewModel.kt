package fr.polytech.bike.ui.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import android.util.Patterns
import fr.polytech.bike.data.LoginRepository
import fr.polytech.bike.data.Result

import fr.polytech.bike.R
import fr.polytech.bike.data.local.LocalDatabase
import fr.polytech.bike.data.model.Utilisateur
import fr.polytech.bike.repository.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class LoginViewModel(private val loginRepository: LoginRepository, private val database: LocalDatabase) : ViewModel() {

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    init {
        _loginForm.value = LoginFormState(null, null, true)
    }

    fun login(username: String, password: String) {
        runBlocking {
            launch {
                val result = loginRepository.login(username, password)

                if (result is Result.Success) {
                    val jwt: String = result.data.jwt
                    val response = ApiClient.userRepository.get()
                    val utilisateur: Utilisateur = response.body()!!
                    _loginResult.value = LoginResult(success = LoggedInUserView(displayName = utilisateur.prenomUtil + " " + utilisateur.nomUtil))
                    CoroutineScope(Dispatchers.IO).launch {
                        database.userDao.delete()
                        database.JWTDao.delete()
                        database.JWTDao.insert(result.data)
                        database.userDao.insert(utilisateur)
                    }
                } else {
                    _loginResult.value = LoginResult(error = R.string.login_failed)
                }
            }
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