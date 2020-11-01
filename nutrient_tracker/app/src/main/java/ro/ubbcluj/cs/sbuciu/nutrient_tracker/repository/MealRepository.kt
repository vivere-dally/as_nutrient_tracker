package ro.ubbcluj.cs.sbuciu.nutrient_tracker.repository

import ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao.FoodDao
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao.MealDao
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.service.MealApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.utils.Result
import java.lang.Exception

class MealRepository(private val mealDao: MealDao) {
    val meals = mealDao.getMeals()

    suspend fun refresh(): Result<Boolean> {
        return try {
            val meals = MealApi.service.getMeals()
            for (meal in meals) {
                mealDao.saveMeal(meal)
            }

            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    //TODO generify repositories, apis and daos
//    suspend fun get
}