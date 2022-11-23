package fr.polytech.bike.map

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import fr.polytech.bike.data.model.Etape

class ShowMapViewModelFactory(): ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ShowMapViewModel::class.java)) {
            return ShowMapViewModel() as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}