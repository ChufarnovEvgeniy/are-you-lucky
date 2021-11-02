package com.chufarnov.areyoulucky.Impls

import com.chufarnov.areyoulucky.domain.GameModel
import com.chufarnov.areyoulucky.domain.entities.*
import kotlin.random.Random

class GameModelImpl : GameModel {

    private lateinit var gameState: GameStateEntity
    private var listener: GameModel.Listener? = null

    private val cellsColors = GameColor.values()
    private val itemTypes = ItemType.values()

    override fun subscribe(listener: GameModel.Listener) {
        this.listener = listener
    }

    override fun unsubscribe() {
        listener = null
    }

    override fun createNewGame() {
        gameState = GameStateEntity(
            INITIAL_LEVEL,
            INITIAL_HEARTS,
            INITIAL_ENERGY,
            INITIAL_MOVES,
            initMap(),
            initAbilitiesSet()
        )

        updateAllValues()
    }

    private fun initMap(): MapEntity {
        val cells = mutableListOf<CellEntity>().apply {
            add(CellEntity(ItemEntity(ItemType.KEY, 0), cellsColors.random()))
        }

        for (i in 1 until CELLS_QUANTITY) {
            cells.add(getRandomCell())
        }

        cells.shuffle()

        return MapEntity(cells)
    }

    private fun getRandomCell() = CellEntity(getRandomItem(), cellsColors.random())

    private fun getRandomItem(): ItemEntity {
        val type = itemTypes[Random.nextInt(1, itemTypes.size)]
        return ItemEntity(
            type,
            getValueForItem(type)
        )
    }

    private fun getValueForItem(type: ItemType) = when (type) {
        ItemType.KEY -> 0
        ItemType.EMPTY -> 0
        ItemType.HEART -> HEARTS_VALUES.random()
        ItemType.ENERGY -> ENERGY_VALUES.random()
        ItemType.MOVE -> MOVE_VALUES.random()
    }

    private fun updateAllValues() {
        listener?.onLevelChange(gameState.level)
        listener?.onHeartsChange(gameState.hearts)
        listener?.onEnergyChange(gameState.energy)
        listener?.onMovesChange(gameState.moves)
        listener?.onMapChange(gameState.map)
    }

    private fun initAbilitiesSet() = AbilitiesSetEntity(
        AbilityEntity(AbilityType.OPEN_CELL, OPEN_CELL_ABILITY_COST),
        AbilityEntity(AbilityType.RESTORE_HEALTH, RESTORE_HEART_ABILITY_COST)
    )

    override fun createGameFromState(state: GameStateEntity) {
        gameState = state
        updateAllValues()
    }

    override fun getGameState(): GameStateEntity = gameState

    override fun useAbility(type: AbilityType) {
        val ability: AbilityEntity
        val effect: () -> Unit

        when (type) {
            AbilityType.OPEN_CELL -> {
                ability = gameState.abilitiesSet.openCell
                effect = { openCell(getRandomCloseCell(), true) }
            }
            AbilityType.RESTORE_HEALTH -> {
                ability = gameState.abilitiesSet.restoreHealth
                effect = { changeHearts(RESTORE_HEART_ABILITY_VALUE) }
            }
        }

        if (ability.energyCost > gameState.energy) return

        changeEnergy(-ability.energyCost)
        effect()
        checkFinishCondition()
    }

    private fun getRandomCloseCell(): Int = gameState.map.cells.mapIndexed { index, value ->
        if (value.state == CellState.CLOSED) index else null
    }.filterNotNull().shuffled().first()

    override fun openCell(position: Int, isFree: Boolean) {
        if (gameState.map.cells[position].state == CellState.OPEN) return
        if (gameState.moves <= 0 && !isFree) return

        if (!isFree) {
            changeMoves(OPEN_CELL_COST)
        }

        listener?.onOpenCell(position)

        gameState.map.cells[position].let {
            it.state = CellState.OPEN
            triggerItemEffect(it.content.type, it.content.value)
        }

        checkFinishCondition()
    }

    private fun triggerItemEffect(type: ItemType, value: Int) {
        when (type) {
            ItemType.KEY -> moveToNextLevel()
            ItemType.EMPTY -> {
            }
            ItemType.HEART -> changeHearts(value)
            ItemType.ENERGY -> changeEnergy(value)
            ItemType.MOVE -> changeMoves(value)
        }
    }

    private fun changeHearts(value: Int) {
        gameState.hearts += value
        listener?.onHeartsChange(gameState.hearts)
    }

    private fun changeEnergy(value: Int) {
        gameState.energy += value
        listener?.onEnergyChange(gameState.energy)
    }

    private fun changeMoves(value: Int) {
        gameState.moves += value
        listener?.onMovesChange(gameState.moves)
    }

    private fun checkFinishCondition() {
        if (gameState.hearts <= 0 || gameState.moves < 0 || gameState.moves <= 0 && gameState.energy <= 0) {
            listener?.onGameFinish()
        }
    }

    private fun moveToNextLevel() {
        gameState.level++
        gameState.moves += MOVES_PER_LEVEL
        gameState.map = initMap()

        listener?.onLevelChange(gameState.level)
        listener?.onMovesChange(gameState.moves)
        listener?.onMapChange(gameState.map)
    }
}