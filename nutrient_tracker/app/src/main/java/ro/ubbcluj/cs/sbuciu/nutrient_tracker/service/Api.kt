package ro.ubbcluj.cs.sbuciu.nutrient_tracker.service

import ro.ubbcluj.cs.sbuciu.nutrient_tracker.domain.model.Entity

interface Api<E : Entity<T>, T> {
    suspend fun save(entity: E): E
    suspend fun update(entityId: T, entity: E): E
    suspend fun delete(entityId: T): E
    fun get(entityId: T): E
    fun get(): List<E>
}