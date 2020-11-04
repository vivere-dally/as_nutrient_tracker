package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils

import android.os.Build
import android.text.format.DateUtils
import androidx.annotation.RequiresApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.config.Environment
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

object MomentJS {
    @RequiresApi(Build.VERSION_CODES.O)
    private var formatter: DateTimeFormatter =
        DateTimeFormatter.ofPattern(Environment.DATE_FORMAT_API, Locale.getDefault())

    @RequiresApi(Build.VERSION_CODES.O)
    fun fromNow(time: String?): CharSequence {
        return DateUtils.getRelativeTimeSpanString(
            LocalDateTime
                .parse(time, formatter)
                .atZone(ZoneId.of("Europe/Bucharest"))
                .toInstant()
                .toEpochMilli(),
            System.currentTimeMillis(),
            DateUtils.SECOND_IN_MILLIS
        )
    }
}