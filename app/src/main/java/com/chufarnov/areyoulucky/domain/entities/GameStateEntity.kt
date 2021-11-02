package com.chufarnov.areyoulucky.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_state")
data class GameStateEntity(
    @ColumnInfo(name = "level")
    var level: Int,
    @ColumnInfo(name = "hearts")
    var hearts: Int,
    @ColumnInfo(name = "energy")
    var energy: Int,
    @ColumnInfo(name = "moves")
    var moves: Int,
    @ColumnInfo(name = "map")
    var map: MapEntity,
    @ColumnInfo(name = "abilitiesSet")
    val abilitiesSet: AbilitiesSetEntity,
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Int = 0
)
