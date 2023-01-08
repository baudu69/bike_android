package fr.polytech.bike.ui.signup

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.databinding.ActivitySignUpBinding
import fr.polytech.bike.repository.ApiClient
import kotlinx.coroutines.*

class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModelFactory: SignUpViewModelFactory
    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: ActivitySignUpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModelFactory = SignUpViewModelFactory()
        this.viewModel = ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]
        this.binding = ActivitySignUpBinding.inflate(layoutInflater)
        this.binding.viewModel = this.viewModel

        this.viewModel.navigateToConnexion.observe(this) {
            if (it) {
                Toast.makeText(this@SignUpActivity, "Inscription réussie", Toast.LENGTH_SHORT)
                    .show()
                this.finish()
            }
        }

        setContentView(this.binding.root)
    }
}