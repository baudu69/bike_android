package fr.polytech.bike.repository

import fr.polytech.bike.data.model.Sortie
import retrofit2.Response
import retrofit2.http.GET

interface SortieRepository {

    @GET("sortie")
    suspend fun getSorties(): Response<List<Sortie>>
}