package fr.polytech.bike.data.model

import java.math.BigDecimal

data class Etape(
    val int: Int,
    val numEtape: Int,
    val nomEtape: String,
    val latitude: Double,
    val longitude: Double,
): java.io.Serializable {
    override fun toString(): String {
        return "Etape(int=$int, numEtape=$numEtape, nomEtape='$nomEtape', latitude=$latitude, longitude=$longitude)"
    }
}
