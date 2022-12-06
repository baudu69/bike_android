package fr.polytech.bike.data

import fr.polytech.bike.data.local.JWTDao
import fr.polytech.bike.data.local.LocalDatabase
import fr.polytech.bike.data.model.JwtResponse
import fr.polytech.bike.data.model.Utilisateur
import fr.polytech.bike.repository.ApiClient
import fr.polytech.bike.ui.login.jwtReponseBody

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {


    companion object Factory {
        var jwt: JwtResponse? = null
        var user: Utilisateur? = null

    }

    val isLoggedIn: Boolean
        get() = jwt != null


    fun logout() {
        jwt = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<JwtResponse> {
        // handle login
        val result: Result<JwtResponse> = dataSource.login(username, password)
        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(jwtResponse: JwtResponse) {
        jwt = jwtResponse
        val bodyBase64: String = jwt!!.jwt.split(".")[1]
        val body: String = String(android.util.Base64.decode(bodyBase64, android.util.Base64.DEFAULT))
        val response: jwtReponseBody = ApiClient.gson.fromJson(body, jwtReponseBody::class.java)
        val utilisateur: Utilisateur = response.user
        user = utilisateur
    }
}