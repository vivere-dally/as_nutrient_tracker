package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.repository.FoodRepository
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.repository.MealRepository
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.room.NutrientTrackerDatabase
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service.FoodApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service.MealApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.OperationStateManager
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.Result

class MealListItemEditViewModel(application: Application) : AndroidViewModel(application) {
    private val mealRepository: MealRepository
    private val foodRepository: FoodRepository

    val operationStateManager: OperationStateManager = OperationStateManager(
        executingInitialState = false,
        completedInitialState = false,
        failureInitialState = null
    )

    init {
        val mealDao = NutrientTrackerDatabase.getDatabase(application, viewModelScope).mealDao()
        mealRepository = MealRepository(mealDao, MealApi)

        val foodDao = NutrientTrackerDatabase.getDatabase(application, viewModelScope).foodDao()
        foodRepository = FoodRepository(foodDao, FoodApi)
    }

    fun saveOrUpdateMeal(meal: Meal) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateMeal - start")
            operationStateManager.mutableExecuting.value = true
            operationStateManager.mutableFailure.value = null
            val result: Result<Meal> = if (meal.id == null) {
                mealRepository.save(meal)
            } else {
                mealRepository.update(meal)
            }

            when (result) {
                is Result.Success -> {
                    Log.d(TAG, "saveOrUpdateMeal - success")
                }
                is Result.Error -> {
                    Log.w(TAG, "saveOrUpdateMeal - failure", result.exception)
                    operationStateManager.mutableFailure.value = result.exception
                }
            }

            operationStateManager.mutableExecuting.value = false
            operationStateManager.mutableCompleted.value = true
        }
    }

    fun getMealById(mealId: Long): LiveData<Meal> {
        Log.v(TAG, "getMealById - start")
        return mealRepository.get(mealId)
    }

    fun deleteMealById(mealId: Long) {
        viewModelScope.launch {
            Log.v(TAG, "deleteMealById - start")
            operationStateManager.mutableExecuting.value = true
            operationStateManager.mutableFailure.value = null
            when (val result: Result<Meal> = mealRepository.delete(mealId)) {
                is Result.Success -> {
                    Log.d(TAG, "deleteMealById - success")
                }

                is Result.Error -> {
                    Log.w(TAG, "deleteMealById - failed", result.exception)
                    operationStateManager.mutableFailure.value = result.exception
                }
            }

            operationStateManager.mutableExecuting.value = false
            operationStateManager.mutableCompleted.value = true
        }
    }
}