package fr.polytech.bike.map

import androidx.fragment.app.Fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider

import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import fr.polytech.bike.R
import fr.polytech.bike.databinding.FragmentShowMapBinding
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class ShowMapFragment : Fragment() {

    private lateinit var binding: FragmentShowMapBinding
    private lateinit var viewModel: ShowMapViewModel
    private lateinit var viewModelFactory: ShowMapViewModelFactory

    private lateinit var mapFragment: SupportMapFragment

    private val callback = OnMapReadyCallback { googleMap ->
        runBlocking {
            launch {
                val sydney = LatLng(-34.0, 151.0)
                googleMap.addMarker(MarkerOptions().position(sydney).title("Marker in Sydney"))
                googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney))
            }
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.viewModelFactory = ShowMapViewModelFactory()
        this.viewModel = ViewModelProvider(this, viewModelFactory)[ShowMapViewModel::class.java]
        this.binding = FragmentShowMapBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val mapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync(callback)
    }
}