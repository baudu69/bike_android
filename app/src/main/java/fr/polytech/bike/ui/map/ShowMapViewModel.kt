package fr.polytech.bike.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.data.model.Etape
import fr.polytech.bike.data.model.Sortie
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class ShowMapViewModel(private val sortieRepository: SortieRepository, private val idSortie: Int) : ViewModel() {

    private val _etapes = MutableLiveData<List<Etape>>()
    val etapes: LiveData<List<Etape>>
        get() = _etapes

    private val ioScope = CoroutineScope(Dispatchers.IO + Job())

    init {
        ioScope.launch {
            _etapes.postValue(sortieRepository.getSortie(idSortie).etapes.sortedBy { it.numEtape })
        }
    }
}