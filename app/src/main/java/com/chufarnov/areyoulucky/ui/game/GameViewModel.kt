package com.chufarnov.areyoulucky.ui.game

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chufarnov.areyoulucky.domain.GameModel
import com.chufarnov.areyoulucky.Impls.INITIAL_LEVEL
import com.chufarnov.areyoulucky.domain.entities.AbilityType
import com.chufarnov.areyoulucky.domain.entities.MapEntity
import com.chufarnov.areyoulucky.domain.repos.CacheGameStateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

private const val NEXT_LEVEL_DELAY = 2000L
private const val GAME_OVER_DELAY = 1000L

@HiltViewModel
class GameViewModel @Inject constructor(
    private val gameModel: GameModel,
    private val cacheGameStateRepo: CacheGameStateRepo
) : ViewModel(), GameModel.Listener {

    private var isNeededToBeCached = true
    private var isMapInitializes = false

    private val _level = MutableLiveData<Int>()
    val level: LiveData<Int> = _level

    private val _hearts = MutableLiveData<Int>()
    val hearts: LiveData<Int> = _hearts

    private val _energy = MutableLiveData<Int>()
    val energy: LiveData<Int> = _energy

    private val _moves = MutableLiveData<Int>()
    val moves: LiveData<Int> = _moves

    private val _map = MutableLiveData<MapEntity>()
    val map: LiveData<MapEntity> = _map

    private val _openCellEvent = MutableLiveData<Int?>()
    val openCellEvent: LiveData<Int?> = _openCellEvent

    private val _gameFinishEvent = MutableLiveData<Int?>()
    val gameFinishEvent: LiveData<Int?> = _gameFinishEvent

    private val _isMapClicksEnabled = MutableLiveData<Boolean>()
    val isMapClicksEnabled: LiveData<Boolean> = _isMapClicksEnabled

    fun onArgumentsExtracted(isNewGame: Boolean) {
        gameModel.subscribe(this)

        if (isNewGame) {
            gameModel.createNewGame()
        } else {
            initFromCachedState()
        }
    }

    private fun initFromCachedState() {
        viewModelScope.launch {
            val cachedState = cacheGameStateRepo.getCachedState()

            withContext(Dispatchers.Main) {
                gameModel.createGameFromState(cachedState)
            }

            cacheGameStateRepo.clear()
        }
    }

    fun onCellClicked(position: Int) {
        gameModel.openCell(position)
    }

    fun onAbilityClicked(type: AbilityType) {
        gameModel.useAbility(type)
    }

    override fun onLevelChange(level: Int) {
        _level.value = level
    }

    override fun onHeartsChange(hearts: Int) {
        _hearts.value = hearts
    }

    override fun onEnergyChange(energy: Int) {
        _energy.value = energy
    }

    override fun onMovesChange(moves: Int) {
        _moves.value = moves
    }

    override fun onMapChange(map: MapEntity) {
        if (level.value != INITIAL_LEVEL && isMapInitializes) {
            _isMapClicksEnabled.value = false

            viewModelScope.launch {
                delay(NEXT_LEVEL_DELAY)

                _map.postValue(map)
                _isMapClicksEnabled.postValue(true)
            }
        } else {
            _map.value = map
            isMapInitializes = true
        }
    }

    override fun onOpenCell(position: Int) {
        _openCellEvent.value = position
    }

    fun onOpenCellComplete() {
        _openCellEvent.value = null
    }

    override fun onGameFinish() {
        _isMapClicksEnabled.value = false
        isNeededToBeCached = false

        viewModelScope.launch {
            delay(GAME_OVER_DELAY)

            _gameFinishEvent.postValue(level.value)
        }
    }

    fun onGameComplete() {
        _gameFinishEvent.value = null
    }

    fun onViewStop() {
        if (isNeededToBeCached) {
            cacheGameState()
        }
    }

    private fun cacheGameState() {
        viewModelScope.launch {
            cacheGameStateRepo.cacheState(gameModel.getGameState())
        }
    }

    fun onViewDestroy() {
        gameModel.unsubscribe()
    }
}