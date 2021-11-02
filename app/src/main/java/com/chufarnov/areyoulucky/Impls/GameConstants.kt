package com.chufarnov.areyoulucky.Impls

const val COLUMNS_NUM = 5
const val ROWS_NUM = 5
const val CELLS_QUANTITY = COLUMNS_NUM * ROWS_NUM

const val INITIAL_LEVEL = 0
const val INITIAL_HEARTS = 10
const val INITIAL_ENERGY = 0
const val INITIAL_MOVES = 20

const val OPEN_CELL_COST = -1
const val MOVES_PER_LEVEL = 10

const val OPEN_CELL_ABILITY_COST = 1
const val RESTORE_HEART_ABILITY_COST = 1

const val RESTORE_HEART_ABILITY_VALUE = 1

val HEARTS_VALUES = listOf(-1, -2)
val ENERGY_VALUES = listOf(1, 2)
val MOVE_VALUES = listOf(-1, 1)