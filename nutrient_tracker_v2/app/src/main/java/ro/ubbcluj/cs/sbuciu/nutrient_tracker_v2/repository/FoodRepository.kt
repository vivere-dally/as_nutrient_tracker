package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.repository

import androidx.lifecycle.LiveData
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.dao.FoodDao
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Food
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service.FoodApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.Result
import java.lang.Exception

class FoodRepository(dao: FoodDao, api: FoodApi) : Repository<Food, Long>(dao, api.foodApiService) {

    val foods = dao.get()

    override suspend fun delete(entityId: Long): Result<Food> {
        return try {
            (dao as FoodDao).delete(entityId)
            Result.Success(
                api.delete(entityId)
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun get(entityId: Long): LiveData<Food> {
        return (dao as FoodDao).get(entityId)
    }

    override fun get(): LiveData<List<Food>> {
        return (dao as FoodDao).get()
    }


}