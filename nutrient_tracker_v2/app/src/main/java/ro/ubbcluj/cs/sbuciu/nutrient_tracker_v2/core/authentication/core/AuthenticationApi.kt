package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.core

import android.util.Log
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.Credentials
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.RetrofitConfig
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.TAG

object AuthenticationApi {
    interface Service {
        @Headers("Content-Type: application/json")
        @POST("login")
        fun login(@Body credentials: Credentials.SimpleCredentials): Call<Unit>

        @Headers("Content-Type: application/json")
        @GET("user/getByUsername")
        fun get(@Query("username") username: String): Call<Credentials.SimpleCredentials>
    }

    private val authenticationApi: Service = RetrofitConfig.retrofit.create(Service::class.java)

    class AuthenticationApiService {
        suspend fun login(credentials: Credentials.SimpleCredentials): Credentials {
            val validatedCredentials = Credentials(credentials)
            val call: Call<Unit> = authenticationApi.login(credentials)
            call.enqueue(object : Callback<Unit> {
                override fun onResponse(
                    call: Call<Unit>,
                    response: Response<Unit>
                ) {
                    Log.v(TAG, "Received accessToken")
                    validatedCredentials.token = response.headers()["accessToken"]
                    RetrofitConfig.tokenInterceptor.credentials = validatedCredentials
                    val secondCall: Call<Credentials.SimpleCredentials> =
                        authenticationApi.get(validatedCredentials.username!!)
                    secondCall.enqueue(object : Callback<Credentials.SimpleCredentials> {
                        override fun onResponse(
                            call: Call<Credentials.SimpleCredentials>,
                            response: Response<Credentials.SimpleCredentials>
                        ) {
                            Log.v(TAG, "Received user id")
                            val user: Credentials.SimpleCredentials? = response.body()
                            if (user != null) {
                                validatedCredentials.id = user.id
                                validatedCredentials.mutableIsLoggedIn.value = true
                            }
                        }

                        override fun onFailure(
                            call: Call<Credentials.SimpleCredentials>,
                            t: Throwable
                        ) {
                            Log.w(TAG, t.message!!)
                        }

                    })
                }

                override fun onFailure(call: Call<Unit>, t: Throwable) {
                    Log.w(TAG, t.message!!)
                }
            })

            return validatedCredentials
        }

        fun logout() {
            RetrofitConfig.tokenInterceptor.credentials = null
        }
    }

    val authenticationApiService = AuthenticationApiService()
}