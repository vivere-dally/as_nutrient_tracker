package ro.ubbcluj.cs.sbuciu.nutrient_tracker.dao

import androidx.room.*
import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Entity

interface Dao<E : Entity<T>, T> {
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