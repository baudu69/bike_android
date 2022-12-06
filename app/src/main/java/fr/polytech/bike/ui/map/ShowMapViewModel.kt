package fr.polytech.bike.ui.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.model.Etape
import fr.polytech.bike.data.model.Sortie

class ShowMapViewModel(private val sortie: Sortie) : ViewModel() {

    private val _etapes = MutableLiveData<List<Etape>>()
    val etapes: LiveData<List<Etape>> = _etapes

    init {
        _etapes.value = sortie.etapes
    }
}