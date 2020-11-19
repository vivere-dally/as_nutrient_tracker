package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.meallist

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.NutrientTrackerDatabase
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseResult
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseViewModel
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.TAG
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.core.AuthenticationApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.authentication.core.CredentialsRepository
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.core.MealApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.core.MealRepository

class MealListViewModel(application: Application, userId: Long) : BaseViewModel(application) {
    private val mealRepository: MealRepository
    private val credentialsRepository: CredentialsRepository
    var meals: LiveData<List<Meal>>

    init {
        val mealDao = NutrientTrackerDatabase.getDatabase(application, viewModelScope).mealDao()
        mealRepository = MealRepository(mealDao, MealApi, userId)
        meals = mealRepository.get()

        val credentialsDao =
            NutrientTrackerDatabase.getDatabase(application, viewModelScope).credentialsDao()
        credentialsRepository =
            CredentialsRepository(credentialsDao, AuthenticationApi.authenticationApiService)
    }

    fun refresh() {
        viewModelScope.launch {
            super.refreshRepository(mealRepository)
        }
    }


    fun logout() {
        viewModelScope.launch {
            Log.v(TAG, "logout - start")
            action.mutableExecuting.value = true
            action.mutableActionError.value = null
            when (val result: BaseResult<Boolean> = credentialsRepository.logout()) {
                is BaseResult.Success -> {
                    when (val result2: BaseResult<Boolean> = mealRepository.delete()) {
                        is BaseResult.Success -> {
                            Log.d(TAG, "logout - success")
                            action.mutableCompleted.value = true
                        }

                        is BaseResult.Error -> {
                            Log.w(TAG, "login - error", result2.exception)
                            action.mutableActionError.value = result2.exception
                        }
                    }
                }

                is BaseResult.Error -> {
                    Log.w(TAG, "login - error", result.exception)
                    action.mutableActionError.value = result.exception
                }
            }

            action.mutableExecuting.value = false
        }
    }

}