package com.example.aplikacjabarmanska.model

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface CocktailDao {
    @Query("DELETE FROM cocktails")
    suspend fun deleteAll()

    @Query("SELECT * FROM cocktails")
    fun observeAll(): Flow<List<Cocktail>>

    @Query("SELECT * FROM cocktails")
    suspend fun getAll(): List<Cocktail>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(cocktails: List<Cocktail>)
}
