package fr.polytech.bike.sorties

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.repository.ApiClient
import fr.polytech.bike.data.model.Sortie
import fr.polytech.bike.repository.SortieRepository
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response

class ListeSortiesViewModel : ViewModel() {
    private val sortieRepository: SortieRepository = ApiClient.sortieRepository

    private val _sorties = MutableLiveData<List<Sortie>>()
    val sorties: LiveData<List<Sortie>>
        get() = _sorties

    init {
        _sorties.value = ArrayList()
        loadSorties()
    }

    private fun loadSorties() {
        runBlocking {
            launch {
                val response: Response<List<Sortie>> = sortieRepository.getSorties()
                if (!response.isSuccessful) {
                    Log.e("ListeSortiesViewModel", "loadSorties: ${response.errorBody()}")
                    return@launch
                }
                _sorties.value = response.body()
            }
        }
    }


}