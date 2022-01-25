package ru.startandroid.vocabulary.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import ru.startandroid.vocabulary.model.dto.SessionOptions
import ru.startandroid.vocabulary.model.repository.PreferencesRepository
import javax.inject.Singleton

@Singleton
class PreferencesRepositoryImpl(
    private val dataStore: DataStore<Preferences>
): PreferencesRepository {

    private val keySessionOptions = stringPreferencesKey("options")

    override suspend fun getLastSessionOptions(): SessionOptions? {
        return dataStore.data.firstOrNull()?.get(keySessionOptions)?.let {
            Json.decodeFromString<SessionOptions>(it)
        }
    }

    override suspend fun saveSessionOptions(options: SessionOptions) {
        dataStore.edit {
            it[keySessionOptions] = Json.encodeToString(options)
        }
    }


}