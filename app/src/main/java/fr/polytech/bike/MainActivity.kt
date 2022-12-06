package fr.polytech.bike

import android.Manifest.permission.*
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import fr.polytech.bike.data.LoginRepository
import fr.polytech.bike.data.bluetooth.ServiceBluetooth
import fr.polytech.bike.databinding.ActivityMainBinding
import fr.polytech.bike.ui.login.LoginActivity

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController : NavController


    private val launcher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode != Activity.RESULT_OK) {
            openLogin()
            return@registerForActivityResult
        }
        this.navController.navigate(R.id.nav_liste_sortie)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.binding = ActivityMainBinding.inflate(layoutInflater)
        this.navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        this.navController = navHostFragment.navController
        startService(Intent(this, ServiceBluetooth::class.java))
        openLogin()
        btnClick()
        setContentView(binding.root)
    }

    fun btnClick() {
        this.binding.btnNavSorties.setOnClickListener {
            this.navigate(R.id.nav_liste_sortie)
        }
        this.binding.btnNavProfil.setOnClickListener {
            this.navigate(R.id.nav_profil)
        }
        this.binding.btnNavLogout.setOnClickListener {
            LoginRepository.user = null
            openLogin()
        }
    }

    private fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        launcher.launch(intent)
    }

    private fun navigate(idSortie: Int) {
        if (this.navController.currentDestination?.id != idSortie) {
            this.navController.navigate(idSortie)
        }
    }



}