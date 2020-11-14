package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.meallist

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.NutrientTrackerDatabase
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.BaseViewModel
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.Meal
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.core.MealApi
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.core.MealRepository

class MealListViewModel(application: Application) : BaseViewModel(application) {
    private val mealRepository: MealRepository
    val meals: LiveData<List<Meal>>

    init {
        val mealDao = NutrientTrackerDatabase.getDatabase(application, viewModelScope).mealDao()
        mealRepository = MealRepository(mealDao, MealApi)
        meals = mealRepository.get()
    }

    fun refresh() {
        viewModelScope.launch {
            super.refreshRepository(mealRepository)
        }
    }
}