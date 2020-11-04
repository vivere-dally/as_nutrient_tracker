package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.converter.LongArrayStringConverter
import java.io.Serializable

@Entity(tableName = "foods")
class Food(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override var id: Long?,

    @ColumnInfo(name = "name")
    var name: String?,

    @ColumnInfo(name = "description")
    var description: String?,

    @ColumnInfo(name = "image")
    var image: String?,

    @TypeConverters(LongArrayStringConverter::class)
    @ColumnInfo(name = "nutrientIds")
    var nutrientIds: LongArray?
) : BaseEntity<Long>, Serializable {
    override fun toString(): String {
        return name ?: "MISSING NAME";
    }
}