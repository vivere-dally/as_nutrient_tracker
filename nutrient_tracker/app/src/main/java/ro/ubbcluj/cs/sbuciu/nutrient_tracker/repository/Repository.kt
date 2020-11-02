package ro.ubbcluj.cs.sbuciu.nutrient_tracker.repository

import androidx.lifecycle.LiveData
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao.Dao
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Entity
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.service.Api
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.utils.Result
import java.lang.Exception

abstract class Repository<E : Entity<T>, T>(
    protected val dao: Dao<E, T>,
    protected val api: Api<E, T>
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