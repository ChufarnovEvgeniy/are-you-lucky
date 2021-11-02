package com.chufarnov.areyoulucky.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.chufarnov.areyoulucky.domain.entities.GameStateEntity
import com.chufarnov.areyoulucky.data.local.converters.AbilitiesSetConverter
import com.chufarnov.areyoulucky.data.local.converters.MapConverter

@Database(entities = [GameStateEntity::class], version = 1, exportSchema = false)
@TypeConverters(MapConverter::class, AbilitiesSetConverter::class)
abstract class GameStateDatabase : RoomDatabase() {
    abstract fun gameStateDao(): GameStateDao
}