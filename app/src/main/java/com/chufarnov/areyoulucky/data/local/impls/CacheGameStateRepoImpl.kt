package com.chufarnov.areyoulucky.data.local.impls

import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.chufarnov.areyoulucky.domain.entities.GameStateEntity
import com.chufarnov.areyoulucky.data.local.GameStateDao
import com.chufarnov.areyoulucky.domain.repos.CacheGameStateRepo
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CacheGameStateRepoImpl @Inject constructor(
    private val gameStateDao: GameStateDao,
    private val externalScope: CoroutineScope,
    private val ioDispatcher: CoroutineDispatcher
) : CacheGameStateRepo {

    override suspend fun cacheState(state: GameStateEntity) {
        withContext(ioDispatcher) {
            externalScope.launch {
                gameStateDao.saveState(state)
            }
        }
    }

    override suspend fun getCachedState(): GameStateEntity = withContext(ioDispatcher) {
        gameStateDao.getState()
    }

    override fun isStateCached(): LiveData<Boolean> {
        return Transformations.map(gameStateDao.getSize()) {
            it != 0
        }
    }

    override suspend fun clear() {
        withContext(ioDispatcher) {
            externalScope.launch {
                gameStateDao.clear()
            }
        }
    }
}