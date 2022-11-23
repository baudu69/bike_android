package fr.polytech.bike.sorties

import android.database.DatabaseUtils
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import fr.polytech.bike.R
import fr.polytech.bike.databinding.ActivityAccueilBinding.inflate
import fr.polytech.bike.databinding.FragmentListeSortiesBinding

class ListeSorties : Fragment() {

    private lateinit var viewModel: ListeSortiesViewModel
    private lateinit var binding: FragmentListeSortiesBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModel = ViewModelProvider(this)[ListeSortiesViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_liste_sorties, container, false)
        binding.viewModel = viewModel
        return binding.root
    }
}