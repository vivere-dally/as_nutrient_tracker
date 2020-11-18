package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.mealedit

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class MealEditViewModelFactory(private val application: Application, private val userId: Long) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MealEditViewModel::class.java)) {
            return MealEditViewModel(application, userId) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}