package fr.polytech.bike.data

import android.util.Log
import fr.polytech.bike.data.model.JwtResponse

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */

class LoginRepository(val dataSource: LoginDataSource) {


    companion object Factory {
        var user: JwtResponse? = null
    }

    // in-memory cache of the loggedInUser object
//    var user: JwtResponse2? = null
//        private set

    val isLoggedIn: Boolean
        get() = user != null


    fun logout() {
        user = null
        dataSource.logout()
    }

    suspend fun login(username: String, password: String): Result<JwtResponse> {
        // handle login
        val result = dataSource.login(username, password)
        if (result is Result.Success) {
            setLoggedInUser(result.data)
        }

        return result
    }

    private fun setLoggedInUser(jwtResponse: JwtResponse) {
        user = jwtResponse
    }
}