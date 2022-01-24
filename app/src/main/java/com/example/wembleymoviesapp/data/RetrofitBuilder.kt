package com.example.wembleymoviesapp.data

import com.example.wembleymoviesapp.Config
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


class RetrofitBuilder @Inject constructor(
    private val loginInterceptor: HttpLoggingInterceptor
) {

    fun createRetrofit(): Retrofit {
        val client: OkHttpClient
        val builder = OkHttpClient.Builder()
        builder.addInterceptor(loginInterceptor)

        client = builder.build()

        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(Config.BASE_URL)
            .client(client)
            .build()
    }
}