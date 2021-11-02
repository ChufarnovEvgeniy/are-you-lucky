package com.chufarnov.areyoulucky.di

import com.chufarnov.areyoulucky.domain.GameModel
import com.chufarnov.areyoulucky.Impls.GameModelImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class InteractorModule {
    @Provides
    @Singleton
    fun provideGameInteractor(): GameModel = GameModelImpl()
}