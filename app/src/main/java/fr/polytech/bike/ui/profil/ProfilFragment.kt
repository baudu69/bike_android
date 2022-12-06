package fr.polytech.bike.ui.profil

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.data.LoginRepository
import fr.polytech.bike.databinding.FragmentProfilBinding
import fr.polytech.bike.repository.ApiClient
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Response
import java.time.LocalDate

class ProfilFragment : Fragment() {
    private lateinit var viewModel: ProfilViewModel
    private lateinit var viewModelFactory: ProfilViewModelFactory
    private lateinit var binding: FragmentProfilBinding
    private var userRepository = ApiClient.userRepository


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModelFactory = ProfilViewModelFactory(LoginRepository.user ?: throw Exception("User not found"))
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfilViewModel::class.java]
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        eventValidate()
        return binding.root
    }

    private fun eventValidate() {
        binding.btnProfilValid.setOnClickListener {
            runBlocking {
                launch {
                    Log.d("ProfilFragment", "eventValidate: ${viewModel.getUserModel()}")
                    val response: Response<Void> = userRepository.update(viewModel.getUserModel())
                    if (response.isSuccessful) {
                        Log.d("ProfilFragment", "eventValidate: ${response.body()}")
                        majUserStocked()
                        Toast.makeText(context, "Profil mis à jour", Toast.LENGTH_SHORT).show()
                    } else {
                        Log.d("ProfilFragment", "error eventValidate: ${response.errorBody()}")
                    }
                }
            }
        }
    }

    private fun majUserStocked() {
        LoginRepository.user?.prenomUtil = viewModel.firstname.value ?: ""
        LoginRepository.user?.nomUtil = viewModel.lastname.value ?: ""
        LoginRepository.user?.poids = viewModel.poids.value?.toDouble() ?: 0.0
        LoginRepository.user?.taille = viewModel.taille.value?.toDouble() ?: 0.0
        LoginRepository.user?.dateNaissance = viewModel.birthdate.value?.let { LocalDate.parse(it) } ?: LocalDate.now()
    }

}