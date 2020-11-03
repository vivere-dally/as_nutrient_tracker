package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.repository.MealRepository
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.room.NutrientTrackerDatabase
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service.MealApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.OperationStateManager
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.Result

class MealListViewModel(application: Application) : AndroidViewModel(application) {
    private val mealRepository: MealRepository
    val operationStateManager: OperationStateManager
    val meals: LiveData<List<Meal>>

    init {
        val mealDao = NutrientTrackerDatabase.getDatabase(application, viewModelScope).mealDao()
        mealRepository = MealRepository(mealDao, MealApi)
        operationStateManager = OperationStateManager(
            executingInitialState = false,
            completedInitialState = false,
            failureInitialState = null
        )
        meals = mealRepository.meals
    }

    fun refresh() {
        viewModelScope.launch {
            Log.v(TAG, "refresh - start")
            operationStateManager.mutableExecuting.value = true
            operationStateManager.mutableFailure.value = null
            when (val result = mealRepository.refresh()) {
                is Result.Success -> {
                    Log.d(TAG, "refresh - success")
                }

                is Result.Error -> {
                    Log.w(TAG, "refresh - failure", result.exception)
                    operationStateManager.mutableFailure.value = result.exception
                }
            }

            operationStateManager.mutableExecuting.value = false
            operationStateManager.mutableCompleted.value = true
        }
    }
}