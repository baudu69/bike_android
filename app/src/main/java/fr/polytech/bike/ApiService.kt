package fr.polytech.bike

import fr.polytech.bike.data.model.JwtResponse
import fr.polytech.bike.data.model.JwtRequest
import fr.polytech.bike.data.model.Sortie
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @POST("auth/signIn")
    suspend fun login(@Body request: JwtRequest): Response<JwtResponse>
}

