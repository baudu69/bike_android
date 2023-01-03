package fr.polytech.bike.data.model

import java.time.LocalDate
import java.time.LocalTime

data class Sortie(
    val id: Int?,
    val numUtil: Int?,
    val dateSortie: LocalDate,
    val heureDepart: LocalTime,
    val heureArrivee: LocalTime,
    val lieuDepart: String,
    val distanceParcourue: Double,
    var etapes: List<Etape>
): java.io.Serializable {
    override fun toString(): String {
        return "Sortie(id=$id, numUtil=$numUtil, dateSortie=$dateSortie, heureDepart=$heureDepart, heureArrivee=$heureArrivee, lieuDepart='$lieuDepart', distanceParcourue=$distanceParcourue)"
    }

    fun titre(): String {
        return "Sortie du $dateSortie au départ de $lieuDepart (distance parcourue : $distanceParcourue km)"
    }

    constructor(dateSortie: LocalDate, heureDepart: LocalTime, heureArrivee: LocalTime, lieuDepart: String, distanceParcourue: Double, etapes: List<Etape>): this(null, null, dateSortie, heureDepart, heureArrivee, lieuDepart, distanceParcourue, etapes)
}
