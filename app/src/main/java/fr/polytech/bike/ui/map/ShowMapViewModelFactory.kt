package fr.polytech.bike.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.data.model.Etape
import fr.polytech.bike.data.model.Sortie

class ShowMapViewModelFactory(var sortie: Sortie): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowMapViewModel::class.java)) {
            sortie.etapes = sortie.etapes.sortedBy { it.numEtape }
            return ShowMapViewModel(sortie) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}