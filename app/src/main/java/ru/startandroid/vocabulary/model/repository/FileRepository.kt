package ru.startandroid.vocabulary.model.repository

import android.net.Uri
import ru.startandroid.vocabulary.model.dto.WordDataNew

interface FileRepository {
    suspend fun importWordsFromFile(uri: Uri): List<WordDataNew>
}