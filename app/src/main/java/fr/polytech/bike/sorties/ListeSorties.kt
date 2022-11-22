package fr.polytech.bike.sorties

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import fr.polytech.bike.R

class ListeSorties : Fragment() {

    companion object {
        fun newInstance() = ListeSorties()
    }

    private lateinit var viewModel: ListeSortiesViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_liste_sorties, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ListeSortiesViewModel::class.java)
        // TODO: Use the ViewModel
    }

}