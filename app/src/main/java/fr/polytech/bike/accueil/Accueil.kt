package fr.polytech.bike.accueil

import android.Manifest
import android.Manifest.permission.BLUETOOTH_CONNECT
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import fr.polytech.bike.data.bluetooth.ServiceBluetooth
import fr.polytech.bike.databinding.ActivityAccueilBinding
import fr.polytech.bike.sorties.SortieActivity
import fr.polytech.bike.ui.login.LoginActivity

class Accueil : AppCompatActivity() {
    var connecte: Boolean = false
    private lateinit var binding: ActivityAccueilBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAccueilBinding.inflate(layoutInflater)
        setContentView(binding.root)
        openLogin()
        Intent(this, ServiceBluetooth::class.java).also { intent ->
            startService(intent)
        }
        binding.btnSorties.setOnClickListener {
            Log.i("Accueil", "Bouton sorties cliqué")
            startActivity(Intent(this, SortieActivity::class.java))
        }
    }

    private fun openLogin() {
        val intent = Intent(this, LoginActivity::class.java)
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode != Activity.RESULT_OK) {
                openLogin()
            }
        }.launch(intent)
    }

    override fun onActivityReenter(resultCode: Int, data: Intent?) {
        super.onActivityReenter(resultCode, data)
        Toast.makeText(applicationContext, "Return", Toast.LENGTH_SHORT).show()
        Log.i("Accueil", "Return to main page")
    }
}