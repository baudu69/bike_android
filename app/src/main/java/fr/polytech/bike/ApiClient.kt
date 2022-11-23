package fr.polytech.bike

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.polytech.bike.adapter.LocalDateAdapter
import fr.polytech.bike.adapter.LocalTimeAdapter
import fr.polytech.bike.repository.SortieRepository
import fr.polytech.bike.ui.login.JwtInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime

object ApiClient {
    private const val BASE_URL = "https://article.ptitbiomed.fr/api/"

    private val gson: Gson by lazy {
        GsonBuilder()
            .registerTypeAdapter(LocalDate::class.java, LocalDateAdapter())
            .registerTypeAdapter(LocalTime::class.java, LocalTimeAdapter())
            .create()
    }

    private val httpClient: OkHttpClient by lazy {
        OkHttpClient.Builder()
            .addInterceptor(JwtInterceptor())
            .build()
    }

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient)
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }

    val sortieRepository: SortieRepository by lazy {
        retrofit.create(SortieRepository::class.java)
    }
}