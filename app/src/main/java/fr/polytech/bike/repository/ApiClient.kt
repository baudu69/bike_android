package fr.polytech.bike.repository

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import fr.polytech.bike.adapter.LocalDateAdapter
import fr.polytech.bike.adapter.LocalTimeAdapter
import fr.polytech.bike.ui.login.JwtInterceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDate
import java.time.LocalTime

object ApiClient {
//    private const val BASE_URL = "http://10.0.2.2:8081/api/"
    private const val BASE_URL = "https://article.ptitbiomed.fr/api/"

    val gson: Gson by lazy {
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

    val authRepository: AuthRepository by lazy {
        retrofit.create(AuthRepository::class.java)
    }

    val sortieApiRepository: SortieApiRepository by lazy {
        retrofit.create(SortieApiRepository::class.java)
    }

    val userRepository: UserRepository by lazy {
        retrofit.create(UserRepository::class.java)
    }
}