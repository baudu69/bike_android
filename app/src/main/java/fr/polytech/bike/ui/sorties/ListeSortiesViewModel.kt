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

class ListeSortiesViewModel(private val sortieRepository: SortieRepository, adapter: SortieViewAdapter) : ViewModel() {

    private val uiScope: CoroutineScope = CoroutineScope(Dispatchers.IO + Job())

    private val _sorties = MutableLiveData<List<Sortie>>()
    val sorties: LiveData<List<Sortie>>
        get() = _sorties

    private val _navigateToDetail = MutableLiveData<Sortie>()
    val navigateToDetail: LiveData<Sortie>
        get() = _navigateToDetail

    private val _confirmDelete = MutableLiveData<Sortie>()
    val confirmDelete: LiveData<Sortie>
        get() = _confirmDelete

    init {
        loadSorties()
        adapter.click.observeForever { sortie ->
            _navigateToDetail.postValue(sortie)
        }
        adapter.longClick.observeForever { sortie ->
            _confirmDelete.postValue(sortie)
        }
    }

    private fun loadSorties() {
        uiScope.launch {
            val sorties = sortieRepository.getSorties()
            _sorties.postValue(sorties)
        }
    }

    fun deleteSortie(it: Sortie?) {
        Log.d("SortieListeFragment", "deleteSortie: $it")
        uiScope.launch {
            it?.let {
                it.id?.let { it1 -> sortieRepository.deleteSortie(it1) }
            }
            _sorties.postValue(sortieRepository.getSorties())
        }

    }


}