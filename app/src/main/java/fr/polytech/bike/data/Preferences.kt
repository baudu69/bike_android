package fr.polytech.bike.data

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey


class Preferences(context: Context) {
    private val masterKeyAlias = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val sharedPreferences = EncryptedSharedPreferences.create(
        context,
        "bike_preferences",
        this.masterKeyAlias,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )
    private val editor:SharedPreferences.Editor = sharedPreferences.edit()

    fun setString(key:String, value:String){
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key:String):String?{
        return sharedPreferences.getString(key, null)
    }

    fun clear(){
        editor.clear()
        editor.apply()
    }
}