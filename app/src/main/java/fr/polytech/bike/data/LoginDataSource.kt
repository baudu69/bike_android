package fr.polytech.bike.data

import fr.polytech.bike.ApiClient
import fr.polytech.bike.data.model.JwtRequest
import fr.polytech.bike.data.model.JwtResponse
import java.io.IOException

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
class LoginDataSource {

    suspend fun login(username: String, password: String): Result<JwtResponse> {
        return try {
            val response = ApiClient.authService.login(JwtRequest(username, password))
            if (response.isSuccessful) {
                Result.Success(response.body()!!)
            } else {
                Result.Error(IOException("Error logging in"))
            }
        } catch (e: Throwable) {
            Result.Error(IOException("Error logging in", e))
        }
    }

    fun logout() {

    }
}