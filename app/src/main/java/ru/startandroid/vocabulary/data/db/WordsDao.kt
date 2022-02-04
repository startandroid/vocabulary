package ru.startandroid.vocabulary.data.db

import androidx.room.*

@Dao
interface WordsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(words: List<WordDataDb>)

    @Query("SELECT * FROM words")
    suspend fun getAll(): List<WordDataDb>

    @Query("UPDATE words SET score = score + 1 WHERE word = :word")
    suspend fun incrementScore(word: String)

    @Transaction
    suspend fun decrementScore(word: String) {
        val score = getScore(word)
        val newScore = if (score > 0) 0 else score - 2
        setScore(word, newScore)
    }

    @Query("UPDATE words SET score = :score WHERE word = :word")
    suspend fun setScore(word: String, score: Int)

    @Query("SELECT score FROM words WHERE word = :word")
    suspend fun getScore(word: String): Int

    @Query("UPDATE words SET lastLearned = :timestamp WHERE word = :word")
    suspend fun setLastLearnedTime(word: String, timestamp: Long)

}