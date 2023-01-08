package fr.polytech.bike.ui.profil

import androidx.databinding.BaseObservable

class ProfilFormState(
    private var _lastnameError: String? = null,
    private var _firstnameError: String? = null,
    private var _passwordError: String? = null,
    private var _birthdateError: String? = null,
    private var _tailleError: String? = null,
    private var _poidsError: String? = null,
    private var _isDataValid: Boolean = true
): BaseObservable() {
    var lastnameError: String? = _lastnameError
        set(value) {
            field = value
            notifyChange()
        }
    var firstnameError: String? = _firstnameError
        set(value) {
            field = value
            notifyChange()
        }
    var passwordError: String? = _passwordError
        set(value) {
            field = value
            notifyChange()
        }
    var birthdateError: String? = _birthdateError
        set(value) {
            field = value
            notifyChange()
        }
    var tailleError: String? = _tailleError
        set(value) {
            field = value
            notifyChange()
        }
    var poidsError: String? = _poidsError
        set(value) {
            field = value
            notifyChange()
        }
    var isDataValid: Boolean = _isDataValid
        set(value) {
            field = value
            notifyChange()
        }
}