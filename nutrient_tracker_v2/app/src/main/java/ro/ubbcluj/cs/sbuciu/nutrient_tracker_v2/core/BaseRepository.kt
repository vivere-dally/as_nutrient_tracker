package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.core

import androidx.lifecycle.LiveData
import java.lang.Exception

abstract class BaseRepository<E : BaseEntity<T>, T>(
    protected val _dao: BaseDao<E, T>,
    protected val _api: BaseApi<E, T>
) {

    suspend fun refresh(): BaseResult<Boolean> {
        return try {
            val entities = _api.get()
            for (entity in entities) {
                _dao.save(entity)
            }

            BaseResult.Success(true)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun save(entity: E): BaseResult<E> {
        return try {
            val savedEntity = _api.save(entity)
            _dao.save(savedEntity)
            return BaseResult.Success(savedEntity)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun update(entity: E): BaseResult<E> {
        return try {
            val updatedEntity = _api.update(entity.id!!, entity)
            _dao.update(updatedEntity)
            return BaseResult.Success(updatedEntity)
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    suspend fun delete(entity: E): BaseResult<E> {
        return try {
            _dao.delete(entity)
            BaseResult.Success(
                _api.delete(entity.id!!)
            )
        } catch (e: Exception) {
            BaseResult.Error(e)
        }
    }

    abstract suspend fun delete(entityId: T): BaseResult<E>

    abstract fun get(entityId: T): LiveData<E>

    abstract fun get(): LiveData<List<E>>
}