package ro.ubbcluj.cs.sbuciu.nutrient_tracker.repository

import androidx.lifecycle.LiveData
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao.MealDao
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.service.Api
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.service.MealApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.utils.Result
import java.lang.Exception

class MealRepository(dao: MealDao, api: MealApi.Service) :
    Repository<Meal, Long>(dao, api as Api<Meal, Long>) {

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