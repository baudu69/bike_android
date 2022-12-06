package fr.polytech.bike.ui.profil

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.model.SignUpRequest
import fr.polytech.bike.data.model.Utilisateur
import fr.polytech.bike.repository.ApiClient
import java.time.LocalDate

class ProfilViewModel(val user: Utilisateur) : ViewModel() {
    val firstname: MutableLiveData<String> = MutableLiveData(user.prenomUtil)
    val lastname: MutableLiveData<String> = MutableLiveData(user.nomUtil)
    val poids: MutableLiveData<String> = MutableLiveData(user.poids.toString())
    val taille: MutableLiveData<String> = MutableLiveData(user.taille.toString())
    val birthdate: MutableLiveData<String> = MutableLiveData(user.dateNaissance.toString())
    val password: MutableLiveData<String> = MutableLiveData()

    fun getUserModel(): SignUpRequest {
        return SignUpRequest(
            firstname.value ?: "",
            lastname.value ?: "",
            null,
            password.value,
            poids.value?.toDouble() ?: 0.0,
            taille.value?.toDouble() ?: 0.0,
            birthdate.value?.let { LocalDate.parse(it) } ?: LocalDate.now()
        )
    }

}