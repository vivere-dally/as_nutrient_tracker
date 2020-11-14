package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.mealedit

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.NutrientTrackerDatabase
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseResult
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseViewModel
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.core.MealApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.core.MealRepository

class MealEditViewModel(application: Application) : BaseViewModel(application) {
    private val mealRepository: MealRepository

    init {
        val mealDao = NutrientTrackerDatabase.getDatabase(application, viewModelScope).mealDao()
        mealRepository = MealRepository(mealDao, MealApi)
    }

    fun saveOrUpdateMeal(meal: Meal) {
        viewModelScope.launch {
            Log.v(TAG, "saveOrUpdateMeal - start")
            action.mutableExecuting.value = true
            action.mutableActionError.value = null
            val result: BaseResult<Meal> = if (meal.id == null) {
                mealRepository.save(meal)
            } else {
                mealRepository.update(meal)
            }

            when (result) {
                is BaseResult.Success -> {
                    Log.d(TAG, "saveOrUpdateMeal - success")
                    action.mutableCompleted.value = true
                }

                is BaseResult.Error -> {
                    Log.w(TAG, "saveOrUpdateMeal - error", result.exception)
                    action.mutableActionError.value = result.exception
                }
            }

            action.mutableExecuting.value = false
        }
    }

    fun getMealById(mealId: Long): LiveData<Meal> {
        Log.v(TAG, "getMealById - start")
        return mealRepository.get(mealId)
    }

    fun deleteMealById(mealId: Long) {
        viewModelScope.launch {
            Log.v(TAG, "deleteMealById - start")
            action.mutableExecuting.value = true
            action.mutableActionError.value = null
            when (val result: BaseResult<Meal> = mealRepository.delete(mealId)) {
                is BaseResult.Success -> {
                    Log.d(TAG, "deleteMealById - success")
                    action.mutableCompleted.value = true
                }

                is BaseResult.Error -> {
                    Log.w(TAG, "deleteMealById - error", result.exception)
                    action.mutableActionError.value = result.exception
                }
            }

            action.mutableExecuting.value = false
        }
    }
}