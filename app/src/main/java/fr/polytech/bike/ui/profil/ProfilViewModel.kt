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
    val profilFormState = ProfilFormState()

    private val _userModel: MutableLiveData<Utilisateur> = MutableLiveData(user)
    val userModel: LiveData<Utilisateur>
        get() = _userModel

    val password: MutableLiveData<String> = MutableLiveData("")
    val birthdate: MutableLiveData<String> = MutableLiveData(user.dateNaissance.toString())

    private val ioScope: CoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private val _message: MutableLiveData<String> = MutableLiveData()
    val message: LiveData<String>
        get() = _message



    fun onValidate() {
        if (profilFormState.lastnameError != null || profilFormState.firstnameError != null
            || profilFormState.passwordError != null || profilFormState.tailleError != null
            || profilFormState.birthdateError != null || profilFormState.poidsError != null) {
            return
        }
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

    fun validateLastname() {
        val text = userModel.value!!.nomUtil
        if (text.isEmpty()) {
            profilFormState.lastnameError = "Le nom ne peut pas être vide"
        } else {
            profilFormState.lastnameError = null
        }
    }

    fun validateFirstname() {
        val text = userModel.value!!.prenomUtil
        if (text.isEmpty()) {
            profilFormState.firstnameError = "Le prénom ne peut pas être vide"
        } else {
            profilFormState.firstnameError = null
        }
    }

    fun validatePassword() {
        val text = password.value!!
        if (text.isEmpty()) {
            return
        }
        if (text.length < 4) {
            profilFormState.passwordError = "Le mot de passe doit contenir au moins 4 caractères"
            return
        }
        if (text.length > 32) {
            profilFormState.passwordError = "Le mot de passe doit contenir au plus 32 caractères"
            return
        }
        profilFormState.passwordError = null
    }

    fun validateBirthdate() {
        val text = birthdate.value!!
        if (text.isEmpty()) {
            profilFormState.birthdateError = "La date de naissance ne peut pas être vide"
            return
        }
        if (text.length != 10) {
            profilFormState.birthdateError = "La date de naissance doit être au format jj/mm/aaaa"
            return
        }
        val date = text.split("-")
        val day = date[2].toInt()
        val month = date[1].toInt()
        val year = date[0].toInt()
        if (day < 1 || day > 31) {
            profilFormState.birthdateError = "Le jour doit être compris entre 1 et 31"
            return
        }
        if (month < 1 || month > 12) {
            profilFormState.birthdateError = "Le mois doit être compris entre 1 et 12"
            return
        }
        if (year < 1900 || year > LocalDate.now().year) {
            profilFormState.birthdateError = "L'année doit être comprise entre 1900 et ${LocalDate.now().year}"
            return
        }
        val localDate = LocalDate.of(year, month, day)
        if (localDate.isAfter(LocalDate.now())) {
            profilFormState.birthdateError = "La date de naissance ne peut pas être dans le futur"
            return
        }

        profilFormState.birthdateError = null
    }

    fun validateTaille() {
        val text = userModel.value!!.taille
        if (text < 0) {
            profilFormState.tailleError = "La taille ne peut pas être négative"
        } else {
            profilFormState.tailleError = null
        }
    }

    fun validatePoids() {
        val text = userModel.value!!.poids
        if (text < 0) {
            profilFormState.poidsError = "Le poids ne peut pas être négatif"
        } else {
            profilFormState.poidsError = null
        }
    }
}