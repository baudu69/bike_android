package fr.polytech.bike.ui.login

import fr.polytech.bike.data.LoginRepository
import okhttp3.Interceptor
import okhttp3.Response

class JwtInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (LoginRepository.jwt != null) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer ${LoginRepository.jwt!!.jwt}")
                .build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
    }
}