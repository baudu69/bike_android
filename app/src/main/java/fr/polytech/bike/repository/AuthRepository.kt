package fr.polytech.bike.repository

import fr.polytech.bike.data.model.JwtResponse
import fr.polytech.bike.data.model.JwtRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthRepository {

    @POST("auth/signIn")
    suspend fun login(@Body request: JwtRequest): Response<JwtResponse>
}

