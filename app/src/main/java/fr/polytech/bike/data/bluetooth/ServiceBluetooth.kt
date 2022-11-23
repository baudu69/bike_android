package fr.polytech.bike.data.bluetooth

import android.annotation.SuppressLint
import android.app.Service
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.IBinder
import android.util.Log
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import me.aflak.bluetooth.Bluetooth
import me.aflak.bluetooth.interfaces.DeviceCallback
import kotlin.concurrent.thread

class ServiceBluetooth : Service() {
    private val bluetooth = Bluetooth(this)

    companion object {
        val message: MutableLiveData<String> = MutableLiveData()
    }


    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    override fun onCreate() {
        super.onCreate()
        thread(start = true) {
            Log.i("ServiceBluetooth", "Service created")
            bluetooth.onStart()
            Log.d("ServiceBluetooth", "Try to connect to device")
            bluetooth.connectToName("VELO_SYMPA")
            Log.d("ServiceBluetooth", "Connected to device")
            bluetooth.setDeviceCallback(object : DeviceCallback {
                @SuppressLint("MissingPermission")
                override fun onDeviceConnected(device: BluetoothDevice) {
                    Log.i("ServiceBluetooth", "Connected to ${device.name}")
                }

                @SuppressLint("MissingPermission")
                override fun onDeviceDisconnected(device: BluetoothDevice, message: String) {
                    Log.i("ServiceBluetooth", "Disconnected from ${device.name}")
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

}