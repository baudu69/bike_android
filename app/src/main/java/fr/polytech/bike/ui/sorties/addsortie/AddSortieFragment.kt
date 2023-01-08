package fr.polytech.bike.ui.sorties.addsortie

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import fr.polytech.bike.data.SortieRepository
import fr.polytech.bike.data.bluetooth.ServiceBluetooth
import fr.polytech.bike.data.model.Etape
import fr.polytech.bike.databinding.FragmentSortieAddBinding
import fr.polytech.bike.repository.ApiClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers

class AddSortieFragment : Fragment() {

    private lateinit var viewModelFactory: AddSortieViewModelFactory
    private lateinit var viewModel: AddSortieViewModel
    private lateinit var binding: FragmentSortieAddBinding

    private val launcherBluetooth =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) {
            for (permission in it) {
                Log.i("PERMISSION", permission.key + " : " + permission.value)
                if (!permission.value) {
                    Toast.makeText(
                        requireContext(),
                        "La permission est nécessaire pour ajouter une sortie",
                        Toast.LENGTH_SHORT
                    ).show()
                    AddSortieFragmentDirections.actionAddSortieFragment2ToNavListeSortie()
                    return@registerForActivityResult
                }
                requireActivity().startService(
                    Intent(
                        requireContext(),
                        ServiceBluetooth::class.java
                    )
                )
            }
        }

    private var requestBluetooth =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == RESULT_OK) {
                Log.d("Bluetooth", "Bluetooth activé")
            } else {
                Toast.makeText(
                    requireContext(),
                    "Le bluetooth est nécessaire pour ajouter une sortie",
                    Toast.LENGTH_SHORT
                ).show()
                AddSortieFragmentDirections.actionAddSortieFragment2ToNavListeSortie()
            }
        }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.viewModelFactory = AddSortieViewModelFactory(SortieRepository(requireContext()))
        this.viewModel = ViewModelProvider(this, viewModelFactory)[AddSortieViewModel::class.java]
        this.binding = FragmentSortieAddBinding.inflate(inflater, container, false)
        this.binding.lifecycleOwner = this
        this.binding.viewModel = this.viewModel
        this.bluetoothPermissions()
        this.binding.mvSortieAdd.onCreate(savedInstanceState)
        this.progressBarControl()
        checkForEnd()
        return this.binding.root
    }

    private fun checkForEnd() {
        this.viewModel.finish.observe(viewLifecycleOwner) {
            if (it) {
                this.findNavController()
                    .navigate(AddSortieFragmentDirections.actionAddSortieFragment2ToNavListeSortie())
            }
        }
    }

    private fun bluetoothPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            launcherBluetooth.launch(
                arrayOf(
                    "android.permission.BLUETOOTH_CONNECT",
                    "android.permission.BLUETOOTH_SCAN"
                )
            )
        } else {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            requestBluetooth.launch(enableBtIntent)
        }
    }

    private fun progressBarControl() {
        this.viewModel.etatTransfert.observe(viewLifecycleOwner) {
            Log.d("Receive state : ", it.toString())
            if (it == null) {
                this.binding.progressBar.isIndeterminate = true
                this.binding.progressBar.progress = 0
                return@observe
            }
            this.binding.progressBar.isIndeterminate = false
            actualizeMap()
            if (it.etapeActuelle == it.nbEtapes) {
                this.binding.progressBar.visibility = ProgressBar.GONE
                return@observe
            }
            this.binding.progressBar.max = it.nbEtapes
            this.binding.progressBar.progress = it.etapeActuelle
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().stopService(Intent(activity, ServiceBluetooth::class.java))
        this.viewModel.reset()
    }

    override fun onResume() {
        super.onResume()
        requireActivity().startService(Intent(activity, ServiceBluetooth::class.java))
        this.viewModel.reset()
    }


    private fun actualizeMap() {
        this.binding.mvSortieAdd.getMapAsync {
            if (viewModel.etapes.isEmpty()) {
                return@getMapAsync
            }
            it.clear()
            for (etape in this.viewModel.etapes) {
                it.addMarker(
                    MarkerOptions()
                        .position(LatLng(etape.latitude, etape.longitude))
                        .title(etape.description())
                )
            }
            if (this.viewModel.etapes.size > 1) {
                val polylineOptions = PolylineOptions()
                for (etape in this.viewModel.etapes) {
                    polylineOptions.add(LatLng(etape.latitude, etape.longitude))
                }
                it.addPolyline(polylineOptions)
            }

            it.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(viewModel.etapes[0].latitude, viewModel.etapes[0].longitude), 10f))
        }
        this.binding.mvSortieAdd.onResume()
    }


}