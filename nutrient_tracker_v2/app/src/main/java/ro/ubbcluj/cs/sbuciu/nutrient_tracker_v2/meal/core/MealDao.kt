package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.core

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseDao
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.Meal

@Dao
abstract class MealDao : BaseDao<Meal, Long> {
    @Query("DELETE FROM meals where id=:entityId")
    abstract suspend fun delete(entityId: Long)

    @Query("DELETE FROM meals")
    abstract suspend fun delete()

    @Query("SELECT * FROM meals WHERE id=:entityId")
    abstract fun get(entityId: Long): LiveData<Meal>

    @Query("SELECT * FROM meals WHERE id=:entityId")
    abstract fun getSynchronous(entityId: Long): Meal

    @Query("SELECT * FROM meals")
    abstract fun get(): LiveData<List<Meal>>

    @Query("SELECT * FROM meals LIMIT :size OFFSET (:page * :size)")
    abstract fun get(page: Int, size: Int): LiveData<List<Meal>>
}