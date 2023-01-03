package fr.polytech.bike.repository

import fr.polytech.bike.data.model.SignUpRequest
import fr.polytech.bike.data.model.Utilisateur
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PUT

interface UserRepository {

    @PUT("utilisateur")
    suspend fun update(@Body request: SignUpRequest): Response<Void>

    @GET("utilisateur")
    suspend fun get(): Response<Utilisateur>

}