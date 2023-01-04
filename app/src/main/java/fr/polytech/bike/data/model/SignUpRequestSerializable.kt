package fr.polytech.bike.data.model

import java.time.LocalDate

class SignUpRequestSerializable(signUpRequest: SignUpRequest): java.io.Serializable {

    private var lastname: String
    private var firstname: String
    private var username: String?
    private var password: String?
    private var poids: Double
    private var taille: Double
    private var birthdate: LocalDate

    init {
        this.lastname = signUpRequest.lastname
        this.firstname = signUpRequest.firstname
        this.username = signUpRequest.username
        this.password = signUpRequest.password
        this.poids = signUpRequest.poids
        this.taille = signUpRequest.taille
        this.birthdate = signUpRequest.birthdate
    }
}