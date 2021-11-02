package com.chufarnov.areyoulucky.domain.entities

import androidx.room.ColumnInfo

enum class AbilityType {
    OPEN_CELL, RESTORE_HEALTH
}

data class AbilityEntity(
    @ColumnInfo(name = "abilityType")
    val abilityType: AbilityType,
    @ColumnInfo(name = "energyCost")
    val energyCost: Int
)
