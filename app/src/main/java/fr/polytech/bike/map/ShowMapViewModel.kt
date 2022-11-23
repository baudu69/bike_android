package fr.polytech.bike.map

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.model.Etape

class ShowMapViewModel() : ViewModel() {

    private val _etapes = MutableLiveData<List<Etape>>()
    val etapes: LiveData<List<Etape>> = _etapes
}