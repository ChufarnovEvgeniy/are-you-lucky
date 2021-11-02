package com.chufarnov.areyoulucky.domain.entities

import androidx.room.ColumnInfo

data class AbilitiesSetEntity(
    @ColumnInfo(name = "openCell")
    val openCell: AbilityEntity,
    @ColumnInfo(name = "restoreHealth")
    val restoreHealth: AbilityEntity
)
