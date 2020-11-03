package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Meal

@Dao
abstract class MealDao : BaseDao<Meal, Long> {
    @Query("DELETE FROM meals where id=:entityId")
    abstract suspend fun delete(entityId: Long)

    @Query("DELETE FROM meals")
    abstract suspend fun delete()

    @Query("SELECT * FROM meals WHERE id=:entityId")
    abstract fun get(entityId: Long): LiveData<Meal>

    @Query("SELECT * FROM meals")
    abstract fun get(): LiveData<List<Meal>>
}