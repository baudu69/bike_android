package fr.polytech.bike.data.model

import androidx.annotation.Keep
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.time.LocalTime

@Keep
@Entity(tableName = "sortie")
data class Sortie(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int?,
    @ColumnInfo(name = "numUtil") val numUtil: Int?,
    @ColumnInfo(name = "dateSortie") val dateSortie: LocalDate,
    @ColumnInfo(name = "heureDepart") val heureDepart: LocalTime,
    @ColumnInfo(name = "heureArrivee") val heureArrivee: LocalTime,
    @ColumnInfo(name = "lieuDepart") val lieuDepart: String,
    @ColumnInfo(name = "distanceParcourue") val distanceParcourue: Double,
    @ColumnInfo(name = "etapes") var etapes: List<Etape>
): java.io.Serializable {
    @ColumnInfo(name = "fav") @Transient var fav: Boolean = false
    override fun toString(): String {
        return "Sortie(id=$id, numUtil=$numUtil, dateSortie=$dateSortie, heureDepart=$heureDepart, heureArrivee=$heureArrivee, lieuDepart='$lieuDepart', distanceParcourue=$distanceParcourue)"
    }

    fun titre(): String {
        return "Sortie du $dateSortie au départ de $lieuDepart (distance parcourue : $distanceParcourue km)"
    }

    constructor(dateSortie: LocalDate, heureDepart: LocalTime, heureArrivee: LocalTime, lieuDepart: String, distanceParcourue: Double, etapes: List<Etape>): this(null, null, dateSortie, heureDepart, heureArrivee, lieuDepart, distanceParcourue, etapes)
}

