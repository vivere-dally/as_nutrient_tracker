package ro.ubbcluj.cs.sbuciu.nutrient_tracker.service

import retrofit2.http.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.config.RetrofitConfig
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Meal

object MealApi {
    interface Service {
        @Headers("Content-Type: application/json")
        @POST("/meal")
        suspend fun save(@Body entity: Meal): Meal

        @Headers("Content-Type: application/json")
        @PUT("/meal/{id}")
        suspend fun update(@Path("id") entityId: Long, @Body entity: Meal): Meal

        @Headers("Content-Type: application/json")
        @DELETE("/meal/{id}")
        suspend fun delete(@Path("id") entityId: Long): Meal

        @Headers("Content-Type: application/json")
        @GET("/meal/{id}")
        fun get(@Path("id") entityId: Long): Meal

        @GET("/meal")
        fun get(): List<Meal>
    }

    val mealApi: Service = RetrofitConfig.retrofit.create(Service::class.java)
}