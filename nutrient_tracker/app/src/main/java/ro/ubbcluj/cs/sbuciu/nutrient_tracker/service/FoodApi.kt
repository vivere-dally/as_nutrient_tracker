package ro.ubbcluj.cs.sbuciu.nutrient_tracker.service

import retrofit2.http.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.config.RetrofitConfig
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Food

object FoodApi {
    interface Service {
        @Headers("Content-Type: application/json")
        @POST("/food")
        suspend fun save(@Body entity: Food): Food

        @Headers("Content-Type: application/json")
        @PUT("/food/{id}")
        suspend fun update(@Path("id") entityId: Long, @Body entity: Food): Food

        @Headers("Content-Type: application/json")
        @DELETE("/food/{id}")
        suspend fun delete(@Path("id") entityId: Long): Food

        @Headers("Content-Type: application/json")
        @GET("/food/{id}")
        fun get(@Path("id") entityId: Long): Food

        @GET("/food")
        fun get(): List<Food>
    }

    val foodApi: Service = RetrofitConfig.retrofit.create(Service::class.java)
}