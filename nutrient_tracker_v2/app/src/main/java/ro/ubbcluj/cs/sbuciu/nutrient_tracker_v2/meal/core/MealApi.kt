package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.core

import android.util.Log
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import retrofit2.http.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.Environment
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.RetrofitConfig
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.Meal

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
    val eventChannel: Channel<String> = Channel()

    class MealWebSocketService : WebSocketListener() {
        override fun onMessage(webSocket: WebSocket, text: String) {
            Log.d(TAG, "onMessage $text")
            runBlocking { eventChannel.send(text) }
        }
    }

    init {
        OkHttpClient().newWebSocket(
            Request.Builder().url("${Environment.WS_URL_API}topic/meal/notification").build(),
            MealWebSocketService()
        )
    }
}