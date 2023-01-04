package fr.polytech.bike.ui.profil

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.LoginRepository
import fr.polytech.bike.data.model.SignUpRequest
import fr.polytech.bike.data.model.Utilisateur
import fr.polytech.bike.repository.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import okhttp3.internal.Util
import retrofit2.Response
import java.time.LocalDate

class ProfilViewModel(user: Utilisateur) : ViewModel() {
    private val _userModel: MutableLiveData<Utilisateur> = MutableLiveData(user)
    val userModel: LiveData<Utilisateur>
        get() = _userModel

    val password: MutableLiveData<String> = MutableLiveData("")
    val birthdate: MutableLiveData<String> = MutableLiveData(user.dateNaissance.toString())


    private val job = Job()
    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO + job)

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String>
        get() = _message

    fun onValidate() {
        ioScope.launch {
            val signUpRequest = userModel.value!!.toSignUpRequest()
            signUpRequest.password = password.value!!
            val response: Response<Void> = ApiClient.userRepository.update(signUpRequest)
            if (response.isSuccessful) {
                _message.postValue("Profil mis à jour")
                LoginRepository.user = userModel.value
            } else {
                _message.postValue("Erreur lors de la mise à jour du profil")
            }
        }
    }

    init {
        checkBirthDate()
    }

    private fun checkBirthDate() {
        birthdate.observeForever {
            try {
                val date = LocalDate.parse(it)
                _userModel.value!!.dateNaissance = date
            } catch (e: Exception) {
                Log.d("ProfilViewModel", "Invalid date")
            }
        }
    }
}