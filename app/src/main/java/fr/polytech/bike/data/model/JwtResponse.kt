package fr.polytech.bike.data.model

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class JwtResponse(
        val jwt: String,
        val username: String
) {
        override fun toString(): String {
                return "JwtResponse(jwt='$jwt', username='$username')"
        }
}