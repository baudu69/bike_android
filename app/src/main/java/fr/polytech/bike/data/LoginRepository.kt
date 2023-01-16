package fr.polytech.bike.data

import android.content.Context
import fr.polytech.bike.data.model.JwtResponse
import fr.polytech.bike.data.model.Utilisateur
import fr.polytech.bike.repository.ApiClient
import fr.polytech.bike.ui.login.JwtInterceptor
import fr.polytech.bike.ui.login.jwtReponseBody
import java.time.LocalDateTime

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val context: Context) {
    private val dataSource = LoginDataSource()
    private val preferences = Preferences(context)

    suspend fun login(username: String, password: String): Result<JwtResponse> {
        val result: Result<JwtResponse> = dataSource.login(username, password)
        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    fun logout() {
        preferences.clear()
        JwtInterceptor.token = null
    }

    private fun setLoggedInUser(jwtResponse: JwtResponse) {
        preferences.setString("token", jwtResponse.jwt)
        preferences.setString("expiration", getExpirationOfJWT(jwtResponse.jwt).toString())
        preferences.setString("user", ApiClient.gson.toJson(getUserOfJWT(jwtResponse.jwt)))
        JwtInterceptor.token = jwtResponse.jwt
    }

    private fun getExpirationOfJWT(token: String): LocalDateTime? {
        val bodyBase64: String = token.split(".")[1]
        val body = String(android.util.Base64.decode(bodyBase64, android.util.Base64.DEFAULT))
        val response: jwtReponseBody = ApiClient.gson.fromJson(body, jwtReponseBody::class.java)
        val expiration: Long = response.exp
        return LocalDateTime.ofEpochSecond(expiration, 0, java.time.ZoneOffset.UTC)
    }

    private fun getUserOfJWT(token: String): Utilisateur {
        val bodyBase64: String = token.split(".")[1]
        val body = String(android.util.Base64.decode(bodyBase64, android.util.Base64.DEFAULT))
        val response: jwtReponseBody = ApiClient.gson.fromJson(body, jwtReponseBody::class.java)
        return response.user
    }
}