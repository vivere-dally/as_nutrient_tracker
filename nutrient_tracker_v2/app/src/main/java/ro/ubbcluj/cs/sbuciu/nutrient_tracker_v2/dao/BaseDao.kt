package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.dao

import androidx.room.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.BaseEntity

interface BaseDao<E : BaseEntity<T>, T> {
    @JvmSuppressWildcards
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun save(entity: E)

    @JvmSuppressWildcards
    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(entity: E)

    @JvmSuppressWildcards
    @Delete
    suspend fun delete(entity: E)
}