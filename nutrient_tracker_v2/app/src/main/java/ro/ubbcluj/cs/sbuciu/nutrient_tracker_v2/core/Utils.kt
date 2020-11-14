package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core

import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object Environment {
    var URL_API: String = "http://192.168.100.3:8080/nutrientTracker/"
    var WS_URL_API: String = "ws://192.168.100.3:8080/nutrientTracker/"
    var DATE_FORMAT_API: String = "YYYY-MM-DDTHH:mm:ss.sssZ"
}

object Moment {
    @RequiresApi(Build.VERSION_CODES.O)
    var prettyFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("EEE MMM d yyyy")

    fun fromNow(epoch: Long): CharSequence {
        return DateUtils.getRelativeTimeSpanString(
            epoch * 1000 - 120 * 60000,
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

val BaseResult<*>.succeeded
    get() = this is BaseResult.Success && data != null

val BaseResult<*>.failed
    get() = this is BaseResult.Error

val BaseResult<*>.loading
    get() = this is BaseResult.Loading

val Any.TAG: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

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

enum class ActionType {
    SAVE,
    UPDATE,
    DELETE
}

class ActionPayload<E : BaseEntity<T>, T>(
    var actionType: ActionType,
    var data: E
)

inline fun <reified T> genericType() = object : TypeToken<T>() {}.type
