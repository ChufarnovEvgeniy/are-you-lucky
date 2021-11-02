package com.chufarnov.areyoulucky.ui

import com.chufarnov.areyoulucky.domain.entities.GameColor
import com.chufarnov.areyoulucky.domain.entities.ItemEntity
import com.chufarnov.areyoulucky.domain.entities.ItemType
import com.example.areyoulucky.R

fun ItemEntity.getDrawable(): Int = when (type) {
    ItemType.KEY -> R.drawable.key_image
    ItemType.HEART -> R.drawable.heart_image
    ItemType.ENERGY -> R.drawable.energy_image
    ItemType.MOVE -> R.drawable.move_image
    ItemType.EMPTY -> 0
}

fun GameColor.getResourceColor(): Int = when (this) {
    GameColor.RED -> R.color.main_red
    GameColor.BLUE -> R.color.main_blue
    GameColor.GREEN -> R.color.main_green
    GameColor.YELLOW -> R.color.main_yellow
}

fun ItemEntity.getColor(): Int = when (type) {
    ItemType.KEY -> GameColor.YELLOW.getResourceColor()
    ItemType.HEART -> GameColor.RED.getResourceColor()
    ItemType.ENERGY -> GameColor.GREEN.getResourceColor()
    ItemType.MOVE -> GameColor.BLUE.getResourceColor()
    ItemType.EMPTY -> 0
}