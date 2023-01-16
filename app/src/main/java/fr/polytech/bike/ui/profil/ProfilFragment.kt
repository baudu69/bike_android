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
import fr.polytech.bike.data.Preferences
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


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModelFactory = ProfilViewModelFactory(Preferences(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory)[ProfilViewModel::class.java]
        binding = FragmentProfilBinding.inflate(inflater, container, false)
        binding.viewModel = viewModel
        this.eventMessage()
        return binding.root
    }

    private fun eventMessage() {
        this.viewModel.message.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }



}