package fr.polytech.bike.ui.sorties

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.repository.ApiClient
import fr.polytech.bike.data.model.Sortie
import fr.polytech.bike.repository.SortieApiRepository
import kotlinx.coroutines.*
import retrofit2.Response

class ListeSortiesViewModel(private val sortieRepository: SortieRepository) : ViewModel() {
    private val _sorties = MutableLiveData<List<Sortie>>()
    val sorties: LiveData<List<Sortie>>
        get() = _sorties

    private val uiScope: CoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    init {
        _sorties.value = ArrayList()
        loadSorties()
    }

    private fun loadSorties() {
        uiScope.launch {
            _sorties.postValue(sortieRepository.getSorties())
        }
    }


}