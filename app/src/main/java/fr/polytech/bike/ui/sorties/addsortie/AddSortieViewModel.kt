package fr.polytech.bike.ui.sorties.addsortie

import android.util.Log
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.data.bluetooth.ServiceBluetooth
import fr.polytech.bike.data.model.Etape
import fr.polytech.bike.data.model.Sortie
import fr.polytech.bike.repository.ApiClient
import kotlinx.coroutines.*
import java.time.LocalDate
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class AddSortieViewModel(private val sortieRepository: SortieRepository) : ViewModel() {

    private val uiScope = CoroutineScope(Dispatchers.IO + Job())

    private val _finish = MutableLiveData<Boolean>()
    val finish: LiveData<Boolean>
        get() = _finish


    val lieuSortie: MutableLiveData<String> = MutableLiveData()

    private var _etapes: MutableList<Etape> = ArrayList()
    val etapes: List<Etape>
        get() = _etapes

    val longueurSortie: LiveData<Double>
        get() = _longueurSortie
    private val _longueurSortie: MutableLiveData<Double> = MutableLiveData()

    private var _etatTransfert: MutableLiveData<EtatTransfert?> = MutableLiveData()
    val etatTransfert: LiveData<EtatTransfert?>
        get() = _etatTransfert

    private var etapesTotal = 0
    private var etapeActuelle = 0

    init {
        reset()
        ServiceBluetooth.message.observeForever { message ->
            try {
                etapesTotal = message.toInt()
                _etatTransfert.postValue(EtatTransfert(0, etapesTotal))
            } catch (e: NumberFormatException) {
                etapeActuelle++
                val etape = ApiClient.gson.fromJson(message, Etape::class.java)
                _etapes.add(etape)
                calculateDistance(_etapes)
                _etatTransfert.postValue(EtatTransfert(etapeActuelle, etapesTotal))
            }
        }
    }

    fun reset() {
        _etatTransfert.postValue(null)
        _longueurSortie.postValue(0.0)
        _finish.postValue(false)
        this._etapes = ArrayList()
        this.etapeActuelle = 0
        this.etapesTotal = 0
    }

    private fun calculateDistance(etapes: List<Etape>) {
        if (etapes.size < 2) {
            _longueurSortie.postValue(0.0)
            return
        }
        var distance = 0.0
        for (i in 0 until etapes.size - 1) {
            val etape1 = etapes[i]
            val etape2 = etapes[i + 1]
            distance += calculateDistanceBetweenTwoPointOnAMap(
                etape1.latitude,
                etape1.longitude,
                etape2.latitude,
                etape2.longitude
            )
        }
        _longueurSortie.postValue(distance)
    }


    private fun calculateDistanceBetweenTwoPointOnAMap(
        latitude1: Double,
        longitude1: Double,
        latitude2: Double,
        longitude2: Double
    ): Double {
        val R = 6371
        val dLat = deg2rad(latitude2 - latitude1)
        val dLon = deg2rad(longitude2 - longitude1)
        val a =
            sin(dLat / 2) * sin(dLat / 2) +
                    cos(deg2rad(latitude1)) * cos(deg2rad(latitude2)) *
                    sin(dLon / 2) * sin(dLon / 2)
        val c = 2 * atan2(sqrt(a), sqrt(1 - a))
        return R * c
    }

    private fun deg2rad(deg: Double): Double {
        return deg * (Math.PI / 180)
    }

    fun validSortie() {
        if (lieuSortie.value == null || lieuSortie.value!!.isEmpty()) {
            Log.d("AddSortieViewModel", "Lieu de la sortie non renseigné")
            return
        }
        val sortie = Sortie(
            LocalDate.now(),
            _etapes[0].heureEtape,
            _etapes[_etapes.size - 1].heureEtape,
            lieuSortie.value!!,
            longueurSortie.value!!,
            _etapes
        )
        uiScope.launch {
            try {
                sortieRepository.insert(sortie)
                Log.d("AddSortieViewModel", "Sortie added")
                _finish.postValue(true)
            } catch (e: Exception) {
                Log.e("AddSortieViewModel", "Error while adding sortie", e)
            }
        }
    }
}

data class EtatTransfert(val etapeActuelle: Int, val nbEtapes: Int)