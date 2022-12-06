package fr.polytech.bike.ui.sorties.addsortie

import android.Manifest.permission.*
import android.content.ComponentName
import android.content.Intent
import android.content.ServiceConnection
import android.os.*
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.BuildConfig
import fr.polytech.bike.data.bluetooth.ServiceBluetooth
import fr.polytech.bike.databinding.FragmentSortieAddBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch

class AddSortieFragment : Fragment() {

    private val defaultScope = CoroutineScope(Dispatchers.Default)
    private val mainHandler = Handler(Looper.getMainLooper())
    private var serviceBluetoothConn: ServiceBluetoothConn? = null
    private var gattServiceData: ServiceBluetooth.DataPlane? = null
    private val myCharacteristicValueChangeNotifications = Channel<String>()

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        defaultScope.launch {
            for (newValue in myCharacteristicValueChangeNotifications) {
                mainHandler.run {
                    Log.d("ServiceBluetooth", "Received notification: $newValue")
                }
            }
        }
        activity?.startForegroundService(Intent(activity, ServiceBluetooth::class.java))
    }

    override fun onStart() {
        super.onStart()

        val latestServiceBluetoothConn = ServiceBluetoothConn()
        if (activity?.bindService(Intent(ServiceBluetooth.DATA_PLANE_ACTION, null, activity, ServiceBluetooth::class.java), latestServiceBluetoothConn, 0) == true) {
            serviceBluetoothConn = latestServiceBluetoothConn
        }
    }

    override fun onStop() {
        super.onStop()

        if (serviceBluetoothConn != null) {
            activity?.unbindService(serviceBluetoothConn!!)
            serviceBluetoothConn = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        // We only want the service around for as long as our app is being run on the device
        activity?.stopService(Intent(activity, ServiceBluetooth::class.java))
    }

    private inner class ServiceBluetoothConn : ServiceConnection {
        override fun onServiceDisconnected(name: ComponentName?) {
            if (BuildConfig.DEBUG && ServiceBluetooth::class.java.name != name?.className) {
                error("Disconnected from unknown service")
            } else {
                gattServiceData = null
            }
        }

        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            if (BuildConfig.DEBUG && ServiceBluetooth::class.java.name != name?.className)
                error("Connected to unknown service")
            else {
                gattServiceData = service as ServiceBluetooth.DataPlane

                gattServiceData?.setMyCharacteristicChangedChannel(myCharacteristicValueChangeNotifications)
            }
        }
    }

}