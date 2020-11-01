package ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey

@androidx.room.Entity(tableName = "foods")
class Food(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override var id: Int?,

    @ColumnInfo(name = "name")
    var name: String,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "image")
    var image: String?,

    @ColumnInfo(name = "nutrientIds")
    var nutrientIds: LongArray
) : Entity