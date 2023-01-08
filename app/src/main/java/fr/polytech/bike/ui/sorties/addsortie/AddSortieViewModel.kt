package fr.polytech.bike.ui.sorties.addsortie

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import fr.polytech.bike.data.bluetooth.ServiceBluetooth
import fr.polytech.bike.data.model.Etape
import fr.polytech.bike.data.model.Sortie
import fr.polytech.bike.repository.ApiClient
import fr.polytech.bike.repository.SortieApiRepository
import kotlinx.coroutines.*
import java.time.LocalDate
import java.time.LocalTime
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

class AddSortieViewModel : ViewModel() {
    private val sortieApiRepository: SortieApiRepository = ApiClient.sortieApiRepository

    private val _finish = MutableLiveData<Boolean>()
    val finish: LiveData<Boolean>
        get() = _finish

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.IO + job)

    val lieuSortie: MutableLiveData<String> = MutableLiveData()
    val dateSortie: MutableLiveData<LocalDate> = MutableLiveData()
    val heureDepart: MutableLiveData<LocalTime> = MutableLiveData()
    val heureArrivee: MutableLiveData<LocalTime> = MutableLiveData()

    private var etapes: MutableList<Etape> = ArrayList()

    val longueurSortie: LiveData<Double>
        get() = _longueurSortie
    private val _longueurSortie: MutableLiveData<Double> = MutableLiveData()


    val etape: LiveData<Etape>
        get() = _etape
    private val _etape: MutableLiveData<Etape> = MutableLiveData()


    val nbEtapes: LiveData<Int>
        get() = _nbEtapes
    private val _nbEtapes: MutableLiveData<Int> = MutableLiveData()

    val etapeActuelle: LiveData<Int>
        get() = _etapeActuelle
    private val _etapeActuelle: MutableLiveData<Int> = MutableLiveData()

    private var lastEtape: Etape? = null

    init {
        _etapeActuelle.postValue(0)
        _longueurSortie.postValue(0.0)

        runBlocking {
            launch {
                ServiceBluetooth.message.observeForever { message ->
                    try {
                        _nbEtapes.postValue(message.toInt())
                    } catch (e: NumberFormatException) {
                        val etape = ApiClient.gson.fromJson(message, Etape::class.java)
                        etapes.add(etape)
                        _etape.postValue(etape)
                        _etapeActuelle.postValue(etapeActuelle.value?.plus(1))
                    }
                }
            }
        }
        this.calculateDistance()
    }

    fun reset() {
        _etapeActuelle.postValue(0)
        _nbEtapes.postValue(0)
        _longueurSortie.postValue(0.0)
        _finish.postValue(false)
        this.etapes = ArrayList()
        this.lastEtape = null
    }

    private fun calculateDistance() {
        this.etape.observeForever {
            if (this.lastEtape != null) {
                val distance = calculateDistanceBetweenTwoPointOnAMap(
                    this.lastEtape!!.latitude,
                    this.lastEtape!!.longitude,
                    it.latitude,
                    it.longitude
                )
                _longueurSortie.postValue(longueurSortie.value?.plus(distance))
            }
            this.lastEtape = it
        }
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
        val sortie = Sortie(
            LocalDate.now(),
            LocalTime.now(),
            LocalTime.now().plusMinutes(10),
            lieuSortie.value!!,
            longueurSortie.value!!,
            etapes
        )
        uiScope.launch {
            val res = sortieApiRepository.addSortie(sortie)
            if (res.isSuccessful) {
                Log.d("AddSortieViewModel", "Sortie added")
                _finish.postValue(true)
            } else {
                Log.d("AddSortieViewModel", "Sortie not added")
            }
        }
    }


}