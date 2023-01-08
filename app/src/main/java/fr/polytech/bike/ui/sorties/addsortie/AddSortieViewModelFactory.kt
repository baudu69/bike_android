package fr.polytech.bike.ui.sorties.addsortie

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.ui.map.ShowMapViewModel

class AddSortieViewModelFactory(private val sortieRepository: SortieRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddSortieViewModel::class.java)) {
            return AddSortieViewModel(sortieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}