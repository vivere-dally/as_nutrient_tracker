package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel

abstract class BaseViewModel(application: Application) : AndroidViewModel(application) {
    val action: BaseAction = BaseAction(
        initExecuting = false,
        initCompleted = false,
        initActionError = null
    )

    protected suspend fun <E : BaseEntity<T>, T> refreshRepository(repository: BaseRepository<E, T>) {
        Log.v(TAG, "refresh - start")
        action.mutableExecuting.value = true
        action.mutableActionError.value = null
        when (val result = repository.refresh()) {
            is BaseResult.Success -> {
                Log.d(TAG, "refresh - success")
            }

            is BaseResult.Error -> {
                Log.w(TAG, "refresh - failure", result.exception)
                action.mutableActionError.value = result.exception
            }
        }

        action.mutableExecuting.value = false
    }
}