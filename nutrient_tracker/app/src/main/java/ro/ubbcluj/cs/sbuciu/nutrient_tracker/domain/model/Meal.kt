package ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.converter.LongArrayStringConverter

@androidx.room.Entity(tableName = "meals")
class Meal(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override var id: Long?,

    @ColumnInfo(name = "comment")
    var comment: String,

    @ColumnInfo(name = "mealDate")
    var mealDate: String,

    @TypeConverters(LongArrayStringConverter::class)
    @ColumnInfo(name = "foodIds")
    var foodIds: LongArray
) : Entity<Long>