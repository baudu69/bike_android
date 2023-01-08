package fr.polytech.bike.ui.sorties

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.ui.sorties.addsortie.AddSortieViewModel

class ListeSortiesViewModelFactory(private val sortieRepository: SortieRepository, private val adapter: SortieViewAdapter): ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListeSortiesViewModel::class.java)) {
            return ListeSortiesViewModel(sortieRepository, adapter) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}