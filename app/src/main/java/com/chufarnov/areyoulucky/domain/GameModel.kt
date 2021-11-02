package com.chufarnov.areyoulucky.domain

import com.chufarnov.areyoulucky.domain.entities.AbilityType
import com.chufarnov.areyoulucky.domain.entities.GameStateEntity
import com.chufarnov.areyoulucky.domain.entities.MapEntity

interface GameModel {

    interface Listener {
        fun onLevelChange(level: Int)
        fun onHeartsChange(hearts: Int)
        fun onEnergyChange(energy: Int)
        fun onMovesChange(moves: Int)
        fun onMapChange(map: MapEntity)
        fun onOpenCell(position: Int)
        fun onGameFinish()
    }

    fun subscribe(listener: Listener)
    fun unsubscribe()
    fun createNewGame()
    fun createGameFromState(state: GameStateEntity)
    fun getGameState(): GameStateEntity
    fun useAbility(type: AbilityType)
    fun openCell(position: Int, isFree: Boolean = false)
}