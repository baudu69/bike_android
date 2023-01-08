package fr.polytech.bike.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import fr.polytech.bike.data.model.JwtResponse

@Dao
interface JWTDao {

    @Insert
    fun insert(response: JwtResponse): Long

    @Query("DELETE FROM jwt_response")
    fun delete()

    @Query("SELECT * FROM jwt_response ORDER BY id DESC LIMIT 1")
    fun getLast(): JwtResponse?
}