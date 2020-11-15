package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.core

import android.util.Log
import androidx.lifecycle.LiveData
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.Meal

import java.lang.Exception

class MealRepository(
    private val dao: MealDao,
    private val api: MealApi
) :
    BaseRepository<Meal, Long>(dao, api.mealApiService) {

    init {
        CoroutineScope(Dispatchers.Main).launch { channelListener() }
    }

    override suspend fun delete(entityId: Long): BaseResult<Meal> {
        return try {
            (_dao as MealDao).delete(entityId)
            BaseResult.Success(
                _api.delete(entityId)
            )
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    override fun get(entityId: Long): LiveData<Meal> {
        return (_dao as MealDao).get(entityId)
    }

    override fun get(): LiveData<List<Meal>> {
        return (_dao as MealDao).get()
    }

    private suspend fun channelListener() {
        while (true) {
            val payload = Gson()
                .fromJson<ActionPayload<Meal, Long>>(
                    api.eventChannel.receive(),
                    genericType<ActionPayload<Meal, Long>>()
                )
            Log.d(TAG, "received $payload")
            handlePayload(payload)
        }
    }

    private suspend fun handlePayload(payload: ActionPayload<Meal, Long>) {
        val meal: Meal = payload.data
        when (payload.actionType) {
            ActionType.SAVE -> _dao.save(meal)
            ActionType.UPDATE -> _dao.update(meal)
            ActionType.DELETE -> _dao.delete(meal)
        }
    }
}