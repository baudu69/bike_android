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
        this.viewModelFactory = AddSortieViewModelFactory()
        this.viewModel = ViewModelProvider(this, viewModelFactory)[AddSortieViewModel::class.java]
        this.binding = FragmentSortieAddBinding.inflate(inflater, container, false)
        this.binding.lifecycleOwner = this
        this.binding.viewModel = this.viewModel
        this.bluetoothPermissions()
        this.binding.mvSortieAdd.onCreate(savedInstanceState)
        this.progressBarControl()
        openMap()
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
        //Indeterminate tant que le bluetooth n'est pas connecté
        var connected = false
        this.binding.progressBar.isIndeterminate = true
        ServiceBluetooth.connected.observe(viewLifecycleOwner) {
            if (it and !connected) {
                connected = true
                this.binding.progressBar.isIndeterminate = false
                this.binding.progressBar.progress = 0
            }
        }

        this.viewModel.nbEtapes.observe(viewLifecycleOwner) {
            this.binding.progressBar.max = it
        }
        this.viewModel.etapeActuelle.observe(viewLifecycleOwner) {
            this.binding.progressBar.progress = it
            if (it == this.binding.progressBar.max && it != 0) {
                Log.i("AddSortieFragment", "Chargement terminé")
                this.binding.btnValidNewSortie.isClickable = true
                this.binding.progressBar.visibility = View.GONE
            }
        }
    }

    override fun onStop() {
        super.onStop()
        requireActivity().stopService(Intent(activity, ServiceBluetooth::class.java))
        this.viewModel.reset()
    }

    private val callback = OnMapReadyCallback { googleMap ->
        var att = 0;
        var etapePrecedente: Etape? = null
        viewModel.etape.observe(viewLifecycleOwner) { etape ->
            val polylineOptions = PolylineOptions()
            googleMap.addMarker(
                MarkerOptions().position(LatLng(etape.latitude, etape.longitude))
                    .title(etape.description())
            )
            polylineOptions.add(LatLng(etape.latitude, etape.longitude))
            if (etapePrecedente != null) {
                polylineOptions.add(LatLng(etapePrecedente!!.latitude, etapePrecedente!!.longitude))
            }
            etapePrecedente = etape
            googleMap.addPolyline(polylineOptions)
            if (att == 0) {
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(LatLng(etape.latitude, etape.longitude), 10f))
            }
            att++
        }
    }

    private fun openMap() {
        MapsInitializer.initialize(requireActivity())
        this.binding.mvSortieAdd.getMapAsync(callback)
        this.binding.mvSortieAdd.onResume()
    }


}