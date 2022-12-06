package fr.polytech.bike.data.model

import java.time.LocalDate

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
data class JwtResponse(
    val jwt: String,
    val user: Utilisateur
) {
    override fun toString(): String {
        return "JwtResponse(jwt='$jwt', username='$user')"
    }
}

data class Utilisateur(var nomUtil: String, var prenomUtil: String, var dateNaissance: LocalDate, var taille: Double, var poids: Double, val login: String) {
    override fun toString(): String {
        return "Utilisateur(nomUtil='$nomUtil', prenomUtil='$prenomUtil')"
    }
}