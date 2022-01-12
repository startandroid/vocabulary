package ru.startandroid.vocabulary.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<WordDataDb>)

    @Query("SELECT * FROM words")
    suspend fun getAll(): List<WordDataDb>

}