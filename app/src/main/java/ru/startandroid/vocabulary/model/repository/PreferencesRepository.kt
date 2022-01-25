package ru.startandroid.vocabulary.model.repository

import ru.startandroid.vocabulary.model.dto.SessionOptions

interface PreferencesRepository {
    suspend fun getLastSessionOptions(): SessionOptions?
    suspend fun saveSessionOptions(options: SessionOptions)
}