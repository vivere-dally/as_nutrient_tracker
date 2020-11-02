package ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.converter

import androidx.room.TypeConverter

class LongArrayStringConverter {
    @TypeConverter
    fun longArrayToString(value: LongArray?): String? {
        if (value != null) {
            return value.joinToString(separator = ",")
        }

        return null
    }

    @TypeConverter
    fun longArrayFromString(value: String?): LongArray? {
        if (value != null) {
            return value.split(",").map { it.trim().toLong() }.toLongArray()
        }

        return null
    }
}