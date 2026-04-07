package com.example.workclockapp

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
interface ClockDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClock(clock: Clock)

    @Update
    suspend fun updateClock(clock: Clock)

    @Query("SELECT * FROM clock WHERE id = :id")
    suspend fun getClockById(id: Int): Clock?

    @Query("SELECT * FROM clock")
    suspend fun getAllClocks(): List<Clock>
}