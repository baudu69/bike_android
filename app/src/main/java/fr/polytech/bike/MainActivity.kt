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
import fr.polytech.bike.data.LoginRepository
import fr.polytech.bike.data.bluetooth.ServiceBluetooth
import fr.polytech.bike.data.local.LocalDatabase
import fr.polytech.bike.data.model.JwtResponse
import fr.polytech.bike.databinding.ActivityMainBinding
import fr.polytech.bike.repository.ApiClient
import fr.polytech.bike.ui.login.LoginActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var localDatabase: LocalDatabase
    private val userRepository = ApiClient.userRepository
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.IO + Job())


    private val launcherLogin =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                openLogin()
                return@registerForActivityResult
            }
            this.navController.navigate(R.id.nav_liste_sortie)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        this.navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        this.navController = navHostFragment.navController
        this.localDatabase = LocalDatabase.getInstance(this)
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
                LoginRepository.jwt = null
                LoginRepository.user = null
                localDatabase.JWTDao.delete()
                localDatabase.userDao.delete()
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
        scope.launch {
            val jwt: JwtResponse? = localDatabase.JWTDao.getLast()
            if (jwt == null) {
                launcherLogin.launch(Intent(context, LoginActivity::class.java))
            } else {
                LoginRepository.jwt = jwt
                val response = userRepository.get()
                if (response.isSuccessful) {
                    LoginRepository.jwt = localDatabase.JWTDao.getLast()
                    LoginRepository.user = response.body()
                    continu.postValue(true)
                } else {
                    launcherLogin.launch(Intent(context, LoginActivity::class.java))
                }
            }

        }
        continu.observe(this) {
            if (it) {
                this.navController.navigate(R.id.nav_liste_sortie)
            }
        }
    }

    private fun navigate(idSortie: Int) {
        if (this.navController.currentDestination?.id != idSortie) {
            this.navController.navigate(idSortie)
        }
    }


}