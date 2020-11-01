package ro.ubbcluj.cs.sbuciu.nutrient_tracker.service

import com.google.gson.GsonBuilder
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.config.Environment
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object Api {
    private val client: OkHttpClient = OkHttpClient
        .Builder()
        .build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit = Retrofit
        .Builder()
        .baseUrl(Environment.URL_API)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
}