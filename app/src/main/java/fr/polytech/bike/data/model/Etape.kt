package fr.polytech.bike.data.model

import java.time.LocalTime

data class Etape(
    val id: Int?,
    val numEtape: Int?,
    val latitude: Double,
    val longitude: Double,
    val distanceFromPrevious: Double,
    val heureEtape: LocalTime,
    val vitesseMoyenneFromPrevious: Double?,
): java.io.Serializable {
    override fun toString(): String {
        return "Etape(int=$id, numEtape=$numEtape, latitude=$latitude, longitude=$longitude)"
    }

    fun description(): String {
        if (numEtape == 1) {
            return "Départ"
        }
        val distanceFormat = String.format("%.2f", distanceFromPrevious)
        val vitesseFormat = String.format("%.2f", vitesseMoyenneFromPrevious)
        return "Etape $numEtape : distance $distanceFormat km, vitesse moyenne $vitesseFormat km/h"
    }
}
