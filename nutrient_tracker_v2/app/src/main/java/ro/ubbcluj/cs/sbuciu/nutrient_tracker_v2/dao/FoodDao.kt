package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Food

@Dao
abstract class FoodDao : BaseDao<Food, Long> {
    @Query("DELETE FROM foods where id=:entityId")
    abstract suspend fun delete(entityId: Long)

    @Query("DELETE FROM foods")
    abstract suspend fun delete()

    @Query("SELECT * FROM foods WHERE id=:entityId")
    abstract fun get(entityId: Long): LiveData<Food>

    @Query("SELECT * FROM foods")
    abstract fun get(): LiveData<List<Food>>
}