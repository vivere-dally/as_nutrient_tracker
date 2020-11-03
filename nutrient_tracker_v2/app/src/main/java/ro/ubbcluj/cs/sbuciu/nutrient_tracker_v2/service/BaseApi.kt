package ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.service

import ro.ubbcluj.cs.sbuciu.nutrient_tracker_v2.domain.model.BaseEntity

interface BaseApi<E : BaseEntity<T>, T> {
    suspend fun save(entity: E): E
    suspend fun update(entityId: T, entity: E): E
    suspend fun delete(entityId: T): E
    suspend fun get(entityId: T): E
    suspend fun get(): List<E>
}