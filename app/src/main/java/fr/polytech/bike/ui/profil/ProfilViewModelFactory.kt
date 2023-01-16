package fr.polytech.bike.ui.profil

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.data.Preferences
import fr.polytech.bike.data.model.Utilisateur
import fr.polytech.bike.ui.sorties.addsortie.AddSortieViewModel

class ProfilViewModelFactory(private val preferences: Preferences): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfilViewModel::class.java)) {
            return ProfilViewModel(preferences = preferences) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}