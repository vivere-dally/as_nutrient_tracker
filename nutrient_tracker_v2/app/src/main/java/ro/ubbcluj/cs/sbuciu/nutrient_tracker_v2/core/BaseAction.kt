package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class BaseAction(
    initExecuting: Boolean,
    initCompleted: Boolean,
    initActionError: Exception?
) {

    val mutableExecuting: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = initExecuting }
    val executing: LiveData<Boolean> = mutableExecuting

    val mutableCompleted: MutableLiveData<Boolean> =
        MutableLiveData<Boolean>().apply { value = initCompleted }
    val completed: LiveData<Boolean> = mutableCompleted

    val mutableActionError: MutableLiveData<Exception> =
        MutableLiveData<Exception>().apply { value = initActionError }
    val actionError: LiveData<Exception> = mutableActionError
}