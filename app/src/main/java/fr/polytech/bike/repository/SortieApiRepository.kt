package fr.polytech.bike.repository

import fr.polytech.bike.data.model.Sortie
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface SortieApiRepository {

    @GET("sortie")
    suspend fun getSorties(): Response<List<Sortie>>

    @POST("sortie")
    suspend fun addSortie(@Body sortie: Sortie): Response<Void>
}