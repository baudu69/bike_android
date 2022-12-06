package fr.polytech.bike.data.model

import java.io.Serializable
import java.time.LocalDate

data class SignUpRequest(val lastname: String, val firstname: String, val username: String?, val password: String?, val poids: Double, val taille: Double, val birthdate: LocalDate) : Serializable {}