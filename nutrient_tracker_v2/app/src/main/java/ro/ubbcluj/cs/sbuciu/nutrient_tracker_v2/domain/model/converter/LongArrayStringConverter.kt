package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.converter

import androidx.room.TypeConverter

class LongArrayStringConverter {
    @TypeConverter
    fun longArrayToString(value: LongArray?): String? {
        if (value != null && value.isNotEmpty()) {
            return value.joinToString(separator = ",")
        }

        return null
    }

    @TypeConverter
    fun longArrayFromString(value: String?): LongArray? {
        if (!value.isNullOrEmpty()) {
            return value.split(",").map { it.trim().toLong() }.toLongArray()
        }

        return null
    }
}