package com.chufarnov.areyoulucky.domain.entities

import androidx.room.ColumnInfo

data class MapEntity(
    @ColumnInfo(name = "cells")
    val cells: List<CellEntity>
)
