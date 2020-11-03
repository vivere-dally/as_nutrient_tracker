package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service

import retrofit2.http.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.config.RetrofitConfig
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Food

object FoodApi {
    interface Service {
        @Headers("Content-Type: application/json")
        @POST("food")
        suspend fun save(@Body entity: Food): Food

        @Headers("Content-Type: application/json")
        @PUT("food/{id}")
        suspend fun update(@Path("id") entityId: Long, @Body entity: Food): Food

        @Headers("Content-Type: application/json")
        @DELETE("food/{id}")
        suspend fun delete(@Path("id") entityId: Long): Food

        @Headers("Content-Type: application/json")
        @GET("food/{id}")
        fun get(@Path("id") entityId: Long): Food

        @Headers("Content-Type: application/json")
        @GET("food")
        fun get(): List<Food>
    }

    private val foodApi: Service = RetrofitConfig.retrofit.create(Service::class.java)

    class FoodApiService : BaseApi<Food, Long> {
        override suspend fun save(entity: Food): Food {
            return foodApi.save(entity)
        }

        override suspend fun update(entityId: Long, entity: Food): Food {
            return foodApi.update(entityId, entity)
        }

        override suspend fun delete(entityId: Long): Food {
            return foodApi.delete(entityId)
        }

        override suspend fun get(entityId: Long): Food {
            return foodApi.get(entityId)
        }

        override suspend fun get(): List<Food> {
            return foodApi.get()
        }
    }

    val foodApiService: BaseApi<Food, Long> = FoodApiService()
}