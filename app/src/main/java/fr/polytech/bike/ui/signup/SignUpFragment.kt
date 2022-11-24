package fr.polytech.bike.ui.signup

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import fr.polytech.bike.R
import fr.polytech.bike.databinding.FragmentSignUpBinding

class SignUpFragment : Fragment() {

    private lateinit var viewModelFactory: SignUpViewModelFactory
    private lateinit var viewModel: SignUpViewModel
    private lateinit var binding: FragmentSignUpBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.viewModelFactory = SignUpViewModelFactory()
        this.viewModel = ViewModelProvider(this, this.viewModelFactory)[SignUpViewModel::class.java]
        this.binding = FragmentSignUpBinding.inflate(inflater, container, false)
        this.binding.viewModel = this.viewModel

        eventValidData()

        return this.binding.root
    }

    private fun eventValidData() {
        val obs: Observer<String> = Observer {
            viewModel.validData()
        }
        viewModel.lastname.observe(viewLifecycleOwner, obs)
        viewModel.firstname.observe(viewLifecycleOwner, obs)
        viewModel.password.observe(viewLifecycleOwner, obs)
        viewModel.confirmPassword.observe(viewLifecycleOwner, obs)
        viewModel.email.observe(viewLifecycleOwner, obs)
        viewModel.birthday.observe(viewLifecycleOwner, obs)
    }


}