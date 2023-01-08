package fr.polytech.bike.data

import android.content.Context
import fr.polytech.bike.data.local.LocalDatabase
import fr.polytech.bike.data.model.Sortie
import fr.polytech.bike.repository.ApiClient

class SortieRepository(context: Context) {
    private val localDatabase = LocalDatabase.getInstance(context)
    private val sortieDao = localDatabase.sortieDao

    private val sortieApiRepository = ApiClient.sortieApiRepository

    suspend fun load() {
        sortieDao.deleteAll()
        val response = sortieApiRepository.getSorties()
        if (response.isSuccessful) {
            response.body()?.let {
                sortieDao.insertAll(it)
            }
        }
    }

    suspend fun getSorties(): List<Sortie> {
        val localSorties = sortieDao.getAll()
        if (localSorties.isNotEmpty()) {
            return localSorties
        }
        val response = sortieApiRepository.getSorties()
        if (response.isSuccessful) {
            val sorties = response.body()!!.sortedBy { it.dateSortie }
            sortieDao.insertAll(sorties)
            return sorties
        }
        return emptyList()
    }

    suspend fun getSortie(id: Int): Sortie {
        val localSortie = sortieDao.getSortie(id)
        if (localSortie != null) {
            return localSortie
        }
        return this.getSorties().first { it.id == id }
    }

    suspend fun insertSortie(sortie: Sortie) {
        val response = sortieApiRepository.addSortie(sortie)
        if (response.isSuccessful) {
            sortieDao.insert(sortie)
        }
    }

    fun clear() {
        sortieDao.deleteAll()
    }
}