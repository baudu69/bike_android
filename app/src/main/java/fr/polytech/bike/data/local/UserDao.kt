package fr.polytech.bike.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import fr.polytech.bike.data.model.JwtResponse
import fr.polytech.bike.data.model.Utilisateur
import okhttp3.internal.Util

@Dao
interface UserDao {
    @Insert
    fun insert(response: Utilisateur): Long

    @Query("DELETE FROM utilisateur")
    fun delete()

    @Query("SELECT * FROM utilisateur ORDER BY login DESC LIMIT 1")
    fun getLast(): Utilisateur?
}