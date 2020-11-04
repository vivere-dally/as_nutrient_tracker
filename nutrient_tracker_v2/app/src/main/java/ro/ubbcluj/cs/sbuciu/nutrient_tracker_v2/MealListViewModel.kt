package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.BaseEntity
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Food
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.repository.FoodRepository
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.repository.MealRepository
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.repository.Repository
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.room.NutrientTrackerDatabase
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service.FoodApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service.MealApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.OperationStateManager
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.Result

class MealListViewModel(application: Application) : AndroidViewModel(application) {
    private val mealRepository: MealRepository
    private val foodRepository: FoodRepository

    val operationStateManager: OperationStateManager = OperationStateManager(
        executingInitialState = false,
        completedInitialState = false,
        failureInitialState = null
    )
    val meals: LiveData<List<Meal>>
    val foods: LiveData<List<Food>>

    init {
        val mealDao = NutrientTrackerDatabase.getDatabase(application, viewModelScope).mealDao()
        mealRepository = MealRepository(mealDao, MealApi)
        meals = mealRepository.meals

        val foodDao = NutrientTrackerDatabase.getDatabase(application, viewModelScope).foodDao()
        foodRepository = FoodRepository(foodDao, FoodApi)
        foods = foodRepository.foods
    }

    fun refresh() {
        viewModelScope.launch {
            refreshRepository(mealRepository)
            refreshRepository(foodRepository)
        }
    }

    private suspend fun <E : BaseEntity<T>, T> refreshRepository(repository: Repository<E, T>) {
        Log.v(TAG, "refresh - start")
        operationStateManager.mutableExecuting.value = true
        operationStateManager.mutableFailure.value = null
        when (val result = repository.refresh()) {
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