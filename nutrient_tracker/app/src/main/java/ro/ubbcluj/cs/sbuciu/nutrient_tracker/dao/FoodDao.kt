package ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Food

@Dao
interface FoodDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveFood(food: Food)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFood(food: Food)

    @Query("DELETE FROM foods WHERE id=:foodId")
    suspend fun deleteFood(foodId: Long)

    @Query("DELETE FROM foods")
    suspend fun deleteFoods()

    @Query("SELECT * FROM foods WHERE id=:foodId")
    fun getFoodById(foodId: Long) : LiveData<Food>

    @Query("SELECT * FROM foods")
    fun getFoods(): LiveData<List<Food>>
}