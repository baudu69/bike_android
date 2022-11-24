package fr.polytech.bike.sorties.addsortie

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.databinding.FragmentSortieAddBinding

class AddSortieFragment : Fragment() {

    private lateinit var viewModelFactory: AddSortieViewModelFactory
    private lateinit var viewModel: AddSortieViewModel
    private lateinit var binding: FragmentSortieAddBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.viewModelFactory = AddSortieViewModelFactory()
        this.viewModel = ViewModelProvider(this, viewModelFactory)[AddSortieViewModel::class.java]
        this.binding = FragmentSortieAddBinding.inflate(inflater, container, false)
        this.binding.viewModel = this.viewModel

        return this.binding.root
    }

}