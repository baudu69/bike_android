package fr.polytech.bike.ui.sorties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.ui.sorties.addsortie.AddSortieViewModel

class ListeSortiesViewModelFactory(private val sortieRepository: SortieRepository): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListeSortiesViewModel::class.java)) {
            return ListeSortiesViewModel(sortieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}