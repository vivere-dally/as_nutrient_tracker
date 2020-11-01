package ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Meal

@Dao
interface MealDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveMeal(meal: Meal)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateMeal(meal: Meal)

    @Query("DELETE FROM meals WHERE id=:mealId")
    suspend fun deleteMeal(mealId: Long)

    @Query("DELETE FROM meals")
    suspend fun deleteMeals()

    @Query("SELECT * FROM meals WHERE id=:mealId")
    fun getMealById(mealId: Long): LiveData<Meal>

    @Query("SELECT * FROM meals ORDER BY mealDate DESC")
    fun getMeals(): LiveData<List<Meal>>
}