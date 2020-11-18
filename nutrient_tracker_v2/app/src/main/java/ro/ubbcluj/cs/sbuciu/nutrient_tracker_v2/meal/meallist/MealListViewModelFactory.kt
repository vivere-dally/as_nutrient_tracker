package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.meallist

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MealListViewModelFactory(private val application: Application, private val userId: Long) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealListViewModel::class.java)) {
            return MealListViewModel(application, userId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}