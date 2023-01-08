package fr.polytech.bike.ui.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.data.model.Etape
import fr.polytech.bike.data.model.Sortie

class ShowMapViewModelFactory(private val sortieRepository: SortieRepository, private val idSortie: Int): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowMapViewModel::class.java)) {
            return ShowMapViewModel(sortieRepository, idSortie) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}