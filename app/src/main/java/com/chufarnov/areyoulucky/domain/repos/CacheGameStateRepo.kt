package com.chufarnov.areyoulucky.domain.repos

import androidx.lifecycle.LiveData
import com.chufarnov.areyoulucky.domain.entities.GameStateEntity

interface CacheGameStateRepo {
    suspend fun cacheState(state: GameStateEntity)
    suspend fun getCachedState(): GameStateEntity
    fun isStateCached(): LiveData<Boolean>
    suspend fun clear()
}