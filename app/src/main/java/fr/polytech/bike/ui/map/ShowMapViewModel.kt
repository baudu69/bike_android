package fr.polytech.bike.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.model.Etape
import fr.polytech.bike.data.model.Sortie

class ShowMapViewModel(private val etapesToAdd: List<Etape>) : ViewModel() {

    private val _etapes = MutableLiveData<List<Etape>>()
    val etapes: LiveData<List<Etape>>
        get() = _etapes

    init {
        _etapes.value = etapesToAdd
    }
}