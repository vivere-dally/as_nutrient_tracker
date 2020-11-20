package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core

import android.util.Log
import androidx.lifecycle.LiveData
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.meal.Meal
import java.lang.Exception
import java.util.*

abstract class BaseRepository<E : BaseEntity<T>, T>(
    protected val _dao: BaseDao<E, T>,
    protected val _api: BaseApi<E, T>
) {

    var entities: LiveData<List<E>>? = null

    suspend fun refresh(): BaseResult<Boolean> {
        return try {
            val entities = _api.get()
            for (entity in entities) {
                _dao.save(entity)
            }

            BaseResult.Success(true)
        } catch (e: Exception) {
            Log.w(TAG, e)
            BaseResult.Success(true)
//            BaseResult.Error(e)
        } finally {
            entities = get()
        }
    }

    suspend fun save(entity: E): BaseResult<E> {
        return try {
            val savedEntity = _api.save(entity)
            _dao.save(savedEntity)
            return BaseResult.Success(savedEntity)
        } catch (e: Exception) {
            try {
                entity.id = Random().nextLong() as T
                _dao.save(entity)
                return BaseResult.Success(entity)
            } catch (e: Exception) {
                BaseResult.Error(e)
            }
        }
    }

    suspend fun update(entity: E): BaseResult<E> {
        return try {
            val updatedEntity = _api.update(entity.id!!, entity)
            _dao.update(updatedEntity)
            return BaseResult.Success(updatedEntity)
        } catch (e: Exception) {
            try {
                _dao.update(entity)
                return BaseResult.Success(entity)
            } catch (e: Exception) {
                BaseResult.Error(e)
            }
        }
    }

    suspend fun delete(entity: E): BaseResult<E> {
        return try {
            _dao.delete(entity)
            BaseResult.Success(
                _api.delete(entity.id!!)
            )
        } catch (e: Exception) {
            try {
                _dao.delete(entity)
                BaseResult.Success(entity)
            } catch (e: Exception) {
                BaseResult.Error(e)
            }
        }
    }

    abstract suspend fun delete(entityId: T): BaseResult<E>

    abstract fun get(entityId: T): LiveData<E>

    abstract fun get(): LiveData<List<E>>
}