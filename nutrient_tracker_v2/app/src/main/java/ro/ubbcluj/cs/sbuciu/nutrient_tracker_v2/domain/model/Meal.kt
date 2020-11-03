package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.converter.LongArrayStringConverter

@Entity(tableName = "meals")
class Meal(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override var id: Long?,

    @ColumnInfo(name = "comment")
    var comment: String?,

    @ColumnInfo(name = "mealDate")
    var mealDate: String?,

    @TypeConverters(LongArrayStringConverter::class)
    @ColumnInfo(name = "foodIds")
    var foodIds: LongArray?
) : BaseEntity<Long>