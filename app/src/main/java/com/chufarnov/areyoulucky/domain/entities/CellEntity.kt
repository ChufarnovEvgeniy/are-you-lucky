package com.chufarnov.areyoulucky.domain.entities

import androidx.room.ColumnInfo

enum class GameColor {
    RED, GREEN, BLUE, YELLOW
}

enum class CellState {
    OPEN, CLOSED
}

data class CellEntity(
    @ColumnInfo(name = "content")
    val content: ItemEntity,
    @ColumnInfo(name = "color")
    val color: GameColor,
    @ColumnInfo(name = "state")
    var state: CellState = CellState.CLOSED
)