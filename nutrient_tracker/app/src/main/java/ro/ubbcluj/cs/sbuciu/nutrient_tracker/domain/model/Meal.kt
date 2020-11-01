package ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@androidx.room.Entity(tableName = "meals")
class Meal(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override var id: Int?,

    @ColumnInfo(name = "comment")
    var comment: String,

    @ColumnInfo(name = "mealDate")
    var mealDate: String,

    @ColumnInfo(name = "foodIds")
    var foodIds: LongArray
) : Entity