package ru.startandroid.vocabulary.model.repository

import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.model.dto.WordDataNew

interface WordRepository {
    suspend fun addNewWords(words: List<WordDataNew>)
    suspend fun getAllWords(): List<WordData>
    suspend fun incrementScore(word: String)
    suspend fun decrementScore(word: String)
    suspend fun learned(word: String)
}