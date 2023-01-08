package fr.polytech.bike.data.local

import androidx.room.*
import fr.polytech.bike.data.model.Sortie

@Dao
interface SortieLocalDao {

    @Query("DELETE FROM sortie")
    fun deleteAll()

    @Query("SELECT * FROM sortie order by fav asc,dateSortie desc")
    fun getAll(): List<Sortie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(sorties: List<Sortie>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(sortie: Sortie)

    @Query("SELECT * FROM sortie WHERE id = :id")
    fun getSortie(id: Int): Sortie?

    @Update
    fun update(sortie: Sortie)
}