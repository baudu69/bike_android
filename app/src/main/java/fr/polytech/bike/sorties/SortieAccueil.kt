package fr.polytech.bike.sorties

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import fr.polytech.bike.R
import fr.polytech.bike.databinding.FragmentSortieAccueilBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SortieAccueil.newInstance] factory method to
 * create an instance of this fragment.
 */
class SortieAccueil : Fragment() {
    private lateinit var binding: FragmentSortieAccueilBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSortieAccueilBinding.inflate(inflater, container, false)
        binding.btnListeSortie.setOnClickListener {
            this.findNavController().navigate(R.id.action_sortieAccueil_to_listeSorties)
        }
        return binding.root
    }
}