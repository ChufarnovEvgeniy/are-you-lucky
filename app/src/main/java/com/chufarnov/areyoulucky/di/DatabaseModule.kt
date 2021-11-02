package com.chufarnov.areyoulucky.di

import android.content.Context
import androidx.room.Room
import com.chufarnov.areyoulucky.data.local.GameStateDao
import com.chufarnov.areyoulucky.data.local.GameStateDatabase
import com.chufarnov.areyoulucky.domain.repos.CacheGameStateRepo
import com.chufarnov.areyoulucky.data.local.impls.CacheGameStateRepoImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {
    @Provides
    @Singleton
    fun provideGameStateDatabase(@ApplicationContext appContext: Context): GameStateDatabase {
        return Room.databaseBuilder(
            appContext,
            GameStateDatabase::class.java,
            "cached_game_state.db"
        ).build()
    }

    @Provides
    @Singleton
    fun provideGameStateDao(database: GameStateDatabase): GameStateDao {
        return database.gameStateDao()
    }

    @Provides
    @Singleton
    fun provideCacheGameStateRepo(gameStateDao: GameStateDao): CacheGameStateRepo {
        return CacheGameStateRepoImpl(
            gameStateDao,
            GameApplication().applicationScope,
            Dispatchers.IO
        )
    }
}