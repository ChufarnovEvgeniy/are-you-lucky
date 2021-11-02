package com.chufarnov.areyoulucky.domain.entities

import androidx.room.ColumnInfo

enum class ItemType {
    KEY, HEART, ENERGY, MOVE, EMPTY
}

data class ItemEntity(
    @ColumnInfo(name = "type")
    val type: ItemType,
    @ColumnInfo(name = "value")
    val value: Int
)