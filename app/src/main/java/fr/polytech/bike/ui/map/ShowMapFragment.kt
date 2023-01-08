package fr.polytech.bike.ui.map

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.gson.reflect.TypeToken
import fr.polytech.bike.R
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.data.model.Etape
import fr.polytech.bike.databinding.FragmentSortieMapBinding
import fr.polytech.bike.repository.ApiClient
import java.lang.reflect.Type

class ShowMapFragment : Fragment() {

    private lateinit var binding: FragmentSortieMapBinding
    private lateinit var viewModel: ShowMapViewModel
    private lateinit var viewModelFactory: ShowMapViewModelFactory

    private val callback = OnMapReadyCallback { googleMap ->
        viewModel.etapes.observe(viewLifecycleOwner) { etapes ->
            val polylineOptions = PolylineOptions()
            etapes.forEach { etape ->
                googleMap.addMarker(MarkerOptions().position(LatLng(etape.latitude, etape.longitude)).title(etape.description()))
                polylineOptions.add(LatLng(etape.latitude, etape.longitude))
            }
            googleMap.addPolyline(polylineOptions)
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(etapes[0].latitude, etapes[0].longitude), 10f))
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val args = ShowMapFragmentArgs.fromBundle(requireArguments())
        this.viewModelFactory = ShowMapViewModelFactory(SortieRepository(requireContext()), args.idSortie)
        this.viewModel = ViewModelProvider(this, viewModelFactory)[ShowMapViewModel::class.java]
        this.binding = FragmentSortieMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}