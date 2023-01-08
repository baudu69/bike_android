package fr.polytech.bike.ui.sorties

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import fr.polytech.bike.R
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.data.model.Sortie
import fr.polytech.bike.databinding.FragmentSortieListeBinding
import fr.polytech.bike.repository.ApiClient
import fr.polytech.bike.repository.SortieApiRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class SortieListeFragment : Fragment() {

    private lateinit var viewModelFactory: ListeSortiesViewModelFactory
    private lateinit var viewModel: ListeSortiesViewModel
    private lateinit var binding: FragmentSortieListeBinding

    private lateinit var adapter: SortieViewAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sortie_liste, container, false)
        adapter = SortieViewAdapter(emptyList(), requireContext(), binding.rvSorties)
        viewModelFactory = ListeSortiesViewModelFactory(SortieRepository(requireContext()), adapter)
        viewModel = ViewModelProvider(this, viewModelFactory)[ListeSortiesViewModel::class.java]
        binding.viewModel = viewModel
        binding.rvSorties.adapter = adapter
        binding.rvSorties.layoutManager = LinearLayoutManager(requireContext())
        eventBtnAddClick()
        adapter()
        return binding.root
    }

    private fun adapter() {
        binding.rvSorties.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL))
        viewModel.sorties.observe(viewLifecycleOwner) {
            adapter.setSorties(it)
        }
        viewModel.navigateToDetail.observe(viewLifecycleOwner) {
            this.findNavController().navigate(
                SortieListeFragmentDirections.actionNavListeSortieToShowMapFragment2(it.id!!)
            )
        }
        viewModel.confirmDelete.observe(viewLifecycleOwner) {
            AlertDialog.Builder(requireContext())
                .setTitle("Supprimer la sortie")
                .setMessage("Voulez-vous vraiment supprimer cette sortie ?")
                .setPositiveButton("Oui") { _, _ ->
                    viewModel.deleteSortie(it)
                }
                .setNegativeButton("Non") { _, _ -> }
                .show()
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