package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.repository

import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.Result
import androidx.lifecycle.LiveData
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.dao.MealDao
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service.MealApi

import java.lang.Exception

class MealRepository(dao: MealDao, api: MealApi) :
    Repository<Meal, Long>(dao, api.mealApiService) {

    val meals = dao.get()

    override suspend fun delete(entityId: Long): Result<Meal> {
        return try {
            (dao as MealDao).delete(entityId)
            Result.Success(
                api.delete(entityId)
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun get(entityId: Long): LiveData<Meal> {
        return (dao as MealDao).get(entityId)
    }

    override fun get(): LiveData<List<Meal>> {
        return (dao as MealDao).get()
    }

}