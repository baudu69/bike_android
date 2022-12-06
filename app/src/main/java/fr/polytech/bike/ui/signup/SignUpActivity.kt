package fr.polytech.bike.ui.signup

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.databinding.ActivitySignUpBinding
import fr.polytech.bike.repository.ApiClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class SignUpActivity : AppCompatActivity() {

    private lateinit var viewModelFactory: SignUpViewModelFactory
    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: ActivitySignUpBinding
    private val authRepository = ApiClient.authRepository
    var valid = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.viewModelFactory = SignUpViewModelFactory()
        this.viewModel = ViewModelProvider(this, viewModelFactory)[SignUpViewModel::class.java]
        this.binding = ActivitySignUpBinding.inflate(layoutInflater)
        this.binding.viewModel = this.viewModel
        this.binding.btnSignupValid.isClickable = false
        eventValidData()
        this.binding.btnSignupValid.setOnClickListener {
            if (!valid) {
                Log.i("SignUpActivity", "Not valid")
                return@setOnClickListener
            }
            Log.i("SignUpActivity", "valid")
            runBlocking {
                launch {
                    val request = authRepository.register(viewModel.getSignupRequest())
                    if (!request.isSuccessful) {
                        Log.e("SignUpActivity", "onCreate error: ${request.errorBody()}")
                        return@launch
                    }
                    Log.d("SignUpActivity", "onCreate: ${request.body()}")
                    Toast.makeText(this@SignUpActivity, "Inscription réussie", Toast.LENGTH_SHORT).show()
                    finish()
                }
            }
        }

        setContentView(this.binding.root)
    }

    private fun eventValidData() {
        val obs: Observer<String> = Observer {
            Log.i("SIGNUP", "eventValidData: $it")
            valid = viewModel.validData() == true
        }
        viewModel.lastname.observe(this, obs)
        viewModel.firstname.observe(this, obs)
        viewModel.password.observe(this, obs)
        viewModel.confirmPassword.observe(this, obs)
        viewModel.username.observe(this, obs)
        viewModel.birthdate.observe(this, obs)
        viewModel.taille.observe(this, obs)
        viewModel.poids.observe(this, obs)
    }
}