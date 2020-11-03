package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.repository

import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.utils.Result
import androidx.lifecycle.LiveData
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.dao.BaseDao
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.BaseEntity
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service.BaseApi
import java.lang.Exception

abstract class Repository<E : BaseEntity<T>, T>(
    protected val dao: BaseDao<E, T>,
    protected val api: BaseApi<E, T>
) {

    suspend fun refresh(): Result<Boolean> {
        return try {
            val entities = api.get()
            for (entity in entities) {
                dao.save(entity)
            }

            Result.Success(true)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun save(entity: E): Result<E> {
        return try {
            val savedEntity = api.save(entity)
            dao.save(savedEntity)
            return Result.Success(savedEntity)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun update(entity: E): Result<E> {
        return try {
            val updatedEntity = api.update(entity.id!!, entity)
            dao.update(updatedEntity)
            return Result.Success(updatedEntity)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    suspend fun delete(entity: E): Result<E> {
        return try {
            dao.delete(entity)
            Result.Success(
                api.delete(entity.id!!)
            )
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

//    suspend fun delete(): Result<Boolean> {
//        return try {
//            val entities = dao.get().value
//            if (entities != null) {
//                for (entity in entities) {
//                    api.delete(entity.id!!)
//                }
//            }
//
//            dao.delete()
//            return Result.Success(true)
//        } catch (e: Exception) {
//            Result.Error(e)
//        }
//    }

    abstract suspend fun delete(entityId: T): Result<E>

    abstract fun get(entityId: T): LiveData<E>

    abstract fun get(): LiveData<List<E>>
}