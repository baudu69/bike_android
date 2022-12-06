package fr.polytech.bike.ui.sorties.addsortie

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.model.Etape
import java.time.LocalDate
import java.time.LocalTime

class AddSortieViewModel : ViewModel() {
    val lieuSortie: MutableLiveData<String> = MutableLiveData()
    val dateSortie: MutableLiveData<LocalDate> = MutableLiveData()
    val heureDepart: MutableLiveData<LocalTime> = MutableLiveData()
    val heureArrivee: MutableLiveData<LocalTime> = MutableLiveData()
    val distance: MutableLiveData<Double> = MutableLiveData()
    val etapes: MutableLiveData<List<Etape>> = MutableLiveData()
}