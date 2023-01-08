package fr.polytech.bike.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.data.LoginDataSource
import fr.polytech.bike.data.LoginRepository
import fr.polytech.bike.data.local.JWTDao
import fr.polytech.bike.data.local.LocalDatabase

/**
 * ViewModel provider factory to instantiate LoginViewModel.
 * Required given LoginViewModel has a non-empty constructor
 */
class LoginViewModelFactory(private val localDatabase: LocalDatabase) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LoginViewModel::class.java)) {
            return LoginViewModel(
                    loginRepository = LoginRepository(
                            dataSource = LoginDataSource()),
                    database = localDatabase
            ) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}