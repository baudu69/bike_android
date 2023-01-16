package fr.polytech.bike

import android.Manifest.permission.*
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.MutableLiveData
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import fr.polytech.bike.data.LoginDataSource
import fr.polytech.bike.data.LoginRepository
import fr.polytech.bike.data.Preferences
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.data.bluetooth.ServiceBluetooth
import fr.polytech.bike.data.local.LocalDatabase
import fr.polytech.bike.data.model.JwtResponse
import fr.polytech.bike.data.model.Utilisateur
import fr.polytech.bike.databinding.ActivityMainBinding
import fr.polytech.bike.repository.ApiClient
import fr.polytech.bike.repository.UserRepository
import fr.polytech.bike.ui.login.JwtInterceptor
import fr.polytech.bike.ui.login.LoginActivity
import kotlinx.coroutines.*
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var sortieRepository: SortieRepository
    private lateinit var loginRepository: LoginRepository
    private val userRepository: UserRepository = ApiClient.userRepository
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + Job())
    private lateinit var preferences: Preferences


    private val launcherLogin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                openLogin()
                return@registerForActivityResult
            }
            scope.launch {
                sortieRepository.load()
            }
            this.navController.navigate(R.id.nav_liste_sortie)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        this.navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        this.navController = navHostFragment.navController

        this.preferences = Preferences(this)
        this.sortieRepository = SortieRepository(this)
        this.loginRepository = LoginRepository(this)
        openLogin()
        btnClick()
        setContentView(binding.root)
    }

    private fun btnClick() {
        this.binding.btnNavSorties.setOnClickListener {
            this.navController.navigate(R.id.nav_liste_sortie)
        }
        this.binding.btnNavProfil.setOnClickListener {
            this.navigate(R.id.nav_profil)
        }
        this.binding.btnNavLogout.setOnClickListener {
            val continu: MutableLiveData<Boolean> = MutableLiveData(false)
            scope.launch {
                loginRepository.logout()
                continu.postValue(true)
            }
            continu.observe(this) {
                if (it) {
                    openLogin()
                }
            }


        }
    }

    private fun openLogin() {
        val continu: MutableLiveData<Boolean> = MutableLiveData(false)
        val context = this
        continu.observe(this) {
            if (it) {
                this.navController.navigate(R.id.nav_liste_sortie)
            }
        }
        scope.launch {
            val jwt: String? = this@MainActivity.preferences.getString("token")
            if (jwt == null) {
                launcherLogin.launch(Intent(context, LoginActivity::class.java))
            } else {
                JwtInterceptor.token = jwt
                val response: Response<Utilisateur> = userRepository.get()
                if (response.isSuccessful) {
                    runBlocking {
                        launch {
                            sortieRepository.load()
                            continu.postValue(true)
                        }

                    }
                } else {
                    launcherLogin.launch(Intent(context, LoginActivity::class.java))
                }
            }

        }

    }

    private fun navigate(idSortie: Int) {
        if (this.navController.currentDestination?.id != idSortie) {
            this.navController.navigate(idSortie)
        }
    }


}