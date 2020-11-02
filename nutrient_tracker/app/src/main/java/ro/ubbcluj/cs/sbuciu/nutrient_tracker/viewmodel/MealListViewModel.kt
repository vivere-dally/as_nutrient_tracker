package ro.ubbcluj.cs.sbuciu.nutrient_tracker.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.repository.MealRepository
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.room.NutrientTrackerDatabase
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.service.MealApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.utils.OperationStateManager
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.utils.Result
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.utils.TAG

class MealListViewModel(application: Application) : AndroidViewModel(application) {
    private val mealRepository: MealRepository
    val operationStateManager: OperationStateManager
    val meals: LiveData<List<Meal>>

    init {
        val mealDao = NutrientTrackerDatabase.getDatabase(application, viewModelScope).mealDao()
        mealRepository = MealRepository(mealDao, MealApi.mealApi)
        operationStateManager = OperationStateManager(
            executingInitialState = false,
            completedInitialState = false,
            failureInitialState = null
        )
        meals = mealRepository.get()
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