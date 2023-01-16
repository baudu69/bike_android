package fr.polytech.bike.ui.login

import fr.polytech.bike.data.LoginRepository
import okhttp3.Interceptor
import okhttp3.Response

class JwtInterceptor: Interceptor {
    companion object {
        var token: String? = null
    }
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (token != null) {
            val newRequest = request.newBuilder()
                .addHeader("Authorization", "Bearer $token")
                .build()
            return chain.proceed(newRequest)
        }
        return chain.proceed(request)
    }
}