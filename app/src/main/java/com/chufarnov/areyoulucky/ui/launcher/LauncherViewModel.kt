package com.chufarnov.areyoulucky.ui.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chufarnov.areyoulucky.domain.repos.CacheGameStateRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val cacheGameStateRepo: CacheGameStateRepo
) : ViewModel() {
    var isGameCached: LiveData<Boolean> = cacheGameStateRepo.isStateCached()

    private val _onNewGameClickedEvent = MutableLiveData<Boolean?>()
    val onNewGameClickedEvent: LiveData<Boolean?> = _onNewGameClickedEvent

    private val _onResumeGameClickedEvent = MutableLiveData<Boolean?>()
    val onResumeGameClickedEvent: LiveData<Boolean?> = _onResumeGameClickedEvent

    fun onNewGameClicked() {
        _onNewGameClickedEvent.value = true

        if (isGameCached.value == true) {
            clearCachedState()
        }
    }

    fun onNewGameClickedFinished() {
        _onNewGameClickedEvent.value = null
    }

    fun onResumeGameClicked() {
        _onResumeGameClickedEvent.value = true
    }

    fun onResumeGameClickedFinished() {
        _onResumeGameClickedEvent.value = null
    }

    private fun clearCachedState() {
        viewModelScope.launch {
            cacheGameStateRepo.clear()
        }
    }
}