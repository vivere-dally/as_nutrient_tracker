package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseEntity

@Entity(tableName = "meals")
class Meal(
    @PrimaryKey
    @ColumnInfo(name = "id")
    override var id: Long?,

    @ColumnInfo(name = "comment")
    var comment: String?,

    @ColumnInfo(name = "date")
    var date: String?,

    @ColumnInfo(name = "dateEpoch")
    var dateEpoch: Long?,

    @ColumnInfo(name = "foods")
    var foods: String?,

    @ColumnInfo(name = "eaten")
    var eaten: Boolean?,

    @ColumnInfo(name = "price")
    var price: Float?,

    @ColumnInfo(name = "userId")
    var userId: Long?
) : BaseEntity<Long>