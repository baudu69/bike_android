package fr.polytech.bike.data.bluetooth

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.bluetooth.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.*
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import fr.polytech.bike.BuildConfig
import fr.polytech.bike.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.SendChannel
import kotlinx.coroutines.launch
import me.aflak.bluetooth.Bluetooth
import me.aflak.bluetooth.interfaces.DeviceCallback
import no.nordicsemi.android.ble.BleManager
import java.util.*
import kotlin.concurrent.thread


class ServiceBluetooth : Service() {
    private val bluetooth = Bluetooth(this)

    companion object {
        val message: MutableLiveData<String> = MutableLiveData()
        val connected: LiveData<Boolean>
            get() = _connected
        private val _connected: MutableLiveData<Boolean> = MutableLiveData()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    @SuppressLint("MissingPermission")
    override fun onCreate() {
        super.onCreate()
        thread(start = true) {
            Log.i("ServiceBluetooth", "Service created")
            bluetooth.onStart()
            Log.d("ServiceBluetooth", "Try to connect to device")
            bluetooth.connectToName("VELO_SYMPA")
            Log.d("ServiceBluetooth", "Connected to device")
            bluetooth.setDeviceCallback(object : DeviceCallback {
                override fun onDeviceConnected(device: BluetoothDevice) {
                    Log.i("ServiceBluetooth", "Connected to ${device.name}")
                    _connected.postValue(true)
                }

                override fun onDeviceDisconnected(device: BluetoothDevice, message: String) {
                    Log.i("ServiceBluetooth", "Disconnected from ${device.name}")
                    _connected.postValue(false)
                }

                override fun onMessage(message: ByteArray) {
                    val mess = String(message)
                    ServiceBluetooth.message.postValue(mess)
                    Log.i("ServiceBluetooth", "Message received: $mess")
                }

                override fun onError(errorCode: Int) {
                    Log.i("ServiceBluetooth", "Error: $errorCode")
                }

                override fun onConnectError(device: BluetoothDevice, message: String) {
                    Log.i("ServiceBluetooth", "Connection error: $message")
                }
            })
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        bluetooth.onStop()
    }
}