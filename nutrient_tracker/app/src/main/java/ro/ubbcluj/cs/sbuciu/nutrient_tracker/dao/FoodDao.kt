package ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Food

@androidx.room.Dao
abstract class FoodDao : Dao<Food, Long> {
    @Query("DELETE FROM foods where id=:entityId")
    abstract suspend fun delete(entityId: Long)

    @Query("DELETE FROM foods")
    abstract suspend fun delete()

    @Query("SELECT * FROM foods WHERE id=:entityId")
    abstract fun get(entityId: Long): LiveData<Food>

    @Query("SELECT * FROM foods")
    abstract fun get(): LiveData<List<Food>>
}