package fr.polytech.bike.data.model

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class Sortie(
    val id: Int,
    val numUtil: Int,
    val dateSortie: LocalDate,
    val heureDepart: LocalTime,
    val heureArrivee: LocalTime,
    val lieuDepart: String,
    val distanceParcourue: BigDecimal,
    val etapes: List<Etape>
) {
    override fun toString(): String {
        return "Sortie(id=$id, numUtil=$numUtil, dateSortie=$dateSortie, heureDepart=$heureDepart, heureArrivee=$heureArrivee, lieuDepart='$lieuDepart', distanceParcourue=$distanceParcourue, etapes=$etapes)"
    }
}
