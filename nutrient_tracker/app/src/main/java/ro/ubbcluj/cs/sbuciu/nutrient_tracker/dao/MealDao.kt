package ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Food
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Meal

@androidx.room.Dao
abstract class MealDao : Dao<Meal, Long> {
    @Query("DELETE FROM meals where id=:entityId")
    abstract suspend fun delete(entityId: Long)

    @Query("DELETE FROM meals")
    abstract suspend fun delete()

    @Query("SELECT * FROM meals WHERE id=:entityId")
    abstract fun get(entityId: Long): LiveData<Meal>

    @Query("SELECT * FROM meals")
    abstract fun get(): LiveData<List<Meal>>
}