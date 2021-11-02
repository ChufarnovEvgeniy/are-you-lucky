package com.chufarnov.areyoulucky.data.local

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.chufarnov.areyoulucky.domain.entities.GameStateEntity

@Dao
interface GameStateDao {
    @Insert(onConflict = REPLACE)
    fun saveState(state: GameStateEntity)

    @Query("SELECT * FROM game_state LIMIT 1")
    fun getState(): GameStateEntity

    @Query("SELECT count(*) FROM game_state")
    fun getSize(): LiveData<Int>

    @Query("DELETE FROM game_state")
    fun clear()
}