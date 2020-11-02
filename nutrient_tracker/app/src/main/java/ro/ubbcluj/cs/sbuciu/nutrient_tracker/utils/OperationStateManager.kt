package ro.ubbcluj.cs.sbuciu.nutrient_tracker.utils

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class OperationStateManager(
    executingInitialState: Boolean,
    completedInitialState: Boolean,
    failureInitialState: Exception?
) {
    val mutableExecuting: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = executingInitialState }
    val mutableCompleted: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = completedInitialState }
    val mutableFailure: MutableLiveData<Exception> =
        MutableLiveData<Exception>().apply { value = failureInitialState }

    val executing: LiveData<Boolean> = mutableExecuting
    val completed: LiveData<Boolean> = mutableCompleted
    val failure: LiveData<Exception> = mutableFailure
}