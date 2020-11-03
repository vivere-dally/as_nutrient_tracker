package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service

import retrofit2.http.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.config.RetrofitConfig
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Meal

object MealApi {
    interface Service {
        @Headers("Content-Type: application/json")
        @POST("meal")
        suspend fun save(@Body entity: Meal): Meal

        @Headers("Content-Type: application/json")
        @PUT("meal/{id}")
        suspend fun update(@Path("id") entityId: Long, @Body entity: Meal): Meal

        @Headers("Content-Type: application/json")
        @DELETE("meal/{id}")
        suspend fun delete(@Path("id") entityId: Long): Meal

        @Headers("Content-Type: application/json")
        @GET("meal/{id}")
        suspend fun get(@Path("id") entityId: Long): Meal

        @Headers("Content-Type: application/json")
        @GET("meal")
        suspend fun get(): List<Meal>
    }

    private val mealApi: Service = RetrofitConfig.retrofit.create(Service::class.java)

    class MealApiService : BaseApi<Meal, Long> {
        override suspend fun save(entity: Meal): Meal {
            return mealApi.save(entity)
        }

        override suspend fun update(entityId: Long, entity: Meal): Meal {
            return mealApi.update(entityId, entity)
        }

        override suspend fun delete(entityId: Long): Meal {
            return mealApi.delete(entityId)
        }

        override suspend fun get(entityId: Long): Meal {
            return mealApi.get(entityId)
        }

        override suspend fun get(): List<Meal> {
            return mealApi.get()
        }
    }

    val mealApiService: BaseApi<Meal, Long> = MealApiService()
}