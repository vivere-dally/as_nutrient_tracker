package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core

import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.*
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.Credentials
import java.lang.reflect.Type
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Environment {
    var URL_API: String = "http://192.168.100.3:8080/nutrientTracker/"
    var WS_URL_API: String = "ws://192.168.100.3:8080/nutrientTracker/"
    var PAGE_SIZE: Int = 10
}

object Moment {
    @RequiresApi(Build.VERSION_CODES.O)
    var prettyFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE MMM d yyyy")

    fun fromNow(epoch: Long): CharSequence {
        return DateUtils.getRelativeTimeSpanString(
            epoch * 1000,
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS
        )
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toDateString(date: String): CharSequence {
        return LocalDateTime.parse(date).format(prettyFormatter)
    }
}

sealed class BaseResult<out R> {
    data class Success<out T>(val data: T) : BaseResult<T>()
    data class Error(val exception: Exception) : BaseResult<Nothing>()
    object Loading : BaseResult<Nothing>()

    override fun toString(): String {
        return when (this) {
            is Success<*> -> "Success[data=$data]"
            is Error -> "Error[exception=$exception]"
            Loading -> "Loading"
        }
    }
}

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

class TokenInterceptor(var credentials: Credentials? = null) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val localCredentials = credentials
        if (localCredentials?.token == null) {
            return chain.proceed(chain.request())
        }

        val requestBuilder = chain
            .request()
            .newBuilder()
            .addHeader("Authorization", localCredentials.token!!)
        if (localCredentials.id == null) {
            return chain.proceed(requestBuilder.build())
        }

        var url = "${Environment.URL_API}user/${localCredentials.id}"
        chain.request().url.pathSegments
            .drop(1)
            .forEach {
                url += "/$it"
            }

        requestBuilder.url(url)
        return chain.proceed(requestBuilder.build())
    }
}

class NullOnEmptyConverterFactory : Converter.Factory() {
    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        val delegate = retrofit.nextResponseBodyConverter<Any>(this, type, annotations)
        return Converter {
            if (it.contentLength() == 0L) {
                Unit
            } else {
                delegate.convert(it)
            }
        }
    }
}

object RetrofitConfig {
    val tokenInterceptor = TokenInterceptor()

    private val client: OkHttpClient = OkHttpClient
        .Builder()
        .addInterceptor(tokenInterceptor)
        .build()

    private var gson = GsonBuilder()
        .setLenient()
        .create()

    val retrofit: Retrofit = Retrofit
        .Builder()
        .baseUrl(Environment.URL_API)
        .addConverterFactory(NullOnEmptyConverterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .client(client)
        .build()
}

enum class ActionType {
    SAVE,
    UPDATE,
    DELETE
}

class PayloadDataType<E : BaseEntity<T>, T>(
    var entity: E,
    var userId: Long
)

class ActionPayload<E : BaseEntity<T>, T>(
    var actionType: ActionType,
    var data: PayloadDataType<E, T>
)

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type
