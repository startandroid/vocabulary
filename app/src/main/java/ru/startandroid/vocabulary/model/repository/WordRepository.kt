package ru.startandroid.vocabulary.model.repository

import ru.startandroid.vocabulary.model.dto.WordDataNew

interface WordRepository {
    suspend fun addNewWords(words: List<WordDataNew>)
}