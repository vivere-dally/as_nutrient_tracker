package ro.ubbcluj.cs.sbuciu.nutrient_tracker.service

import retrofit2.http.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Meal

object MealApi {
    interface Service {
        @Headers("Content-Type: application/json")
        @POST("/meal")
        suspend fun saveMeal(@Body meal: Meal): Meal

        @Headers("Content-Type: application/json")
        @PUT("/meal/{id}")
        suspend fun updateMeal(@Path("id") mealId: Long, @Body meal: Meal): Meal

        @Headers("Content-Type: application/json")
        @DELETE("/meal/{id}")
        suspend fun deleteMeal(@Path("id") mealId: Long): Meal

        @Headers("Content-Type: application/json")
        @GET("/meal/{id}")
        suspend fun getMealById(@Path("id") mealId: Long): Meal

        @GET("/meal")
        suspend fun getMeals(): List<Meal>
    }

    var service: Service = Api.retrofit.create(Service::class.java)
}