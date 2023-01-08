package fr.polytech.bike.ui.sorties

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import fr.polytech.bike.R
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.data.model.Sortie
import fr.polytech.bike.databinding.FragmentSortieListeBinding
import fr.polytech.bike.repository.ApiClient
import fr.polytech.bike.repository.SortieApiRepository

class SortieListeFragment : Fragment() {

    private lateinit var viewModelFactory: ListeSortiesViewModelFactory
    private lateinit var viewModel: ListeSortiesViewModel
    private lateinit var binding: FragmentSortieListeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewModelFactory = ListeSortiesViewModelFactory(SortieRepository(requireContext()))
        viewModel = ViewModelProvider(this, viewModelFactory)[ListeSortiesViewModel::class.java]
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sortie_liste, container, false)
        binding.viewModel = viewModel
        loadListe()
        eventListClick()
        eventBtnAddClick()
        return binding.root
    }

    private fun loadListe() {
        viewModel.sorties.observe(viewLifecycleOwner) { sorties ->
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, sorties.map { it.titre() })
            binding.lvSorties.adapter = adapter
        }
    }

    private fun eventListClick() {
        binding.lvSorties.setOnItemClickListener { _, _, position, _ ->
            val sortie: Sortie? = viewModel.sorties.value?.get(position)
            if (sortie == null) {
                Log.e("ListeSorties", "Click on item $position but no sortie found")
                return@setOnItemClickListener
            }
            this.findNavController().navigate(
                SortieListeFragmentDirections.actionNavListeSortieToShowMapFragment2(
                    sortie.id!!
                ),
            )

        }
    }

    private fun eventBtnAddClick() {
        binding.btnSortieAdd.setOnClickListener {
            this.findNavController().navigate(
                SortieListeFragmentDirections.actionNavListeSortieToAddSortieFragment2()
            )
        }
    }

}