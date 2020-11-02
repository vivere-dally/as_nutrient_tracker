package ro.ubbcluj.cs.sbuciu.nutrient_tracker.config

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitConfig {
    private val client: OkHttpClient = OkHttpClient
        .Builder()
        .build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(Environment.URL_API)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
}