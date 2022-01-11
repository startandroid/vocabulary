package ru.startandroid.vocabulary.data.repository

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.startandroid.vocabulary.data.ResourceProvider
import ru.startandroid.vocabulary.model.dto.WordDataNew
import ru.startandroid.vocabulary.model.repository.FileRepository
import javax.inject.Singleton

@Singleton
class FileRepositoryImpl(private val resourceProvider: ResourceProvider) : FileRepository {
    override suspend fun importWordsFromFile(uri: Uri): List<WordDataNew> {
        return withContext(Dispatchers.IO) {
            // TODO handle errors
            resourceProvider.openInputStream(uri)
                ?.readBytes()
                ?.toString(Charsets.UTF_8)
                ?.split("\n")
                ?.map {
                    val data = it.split("###")
                    WordDataNew(
                        word = data[0],
                        translate = data[1],
                        tags = data[2].split(",").toSet()
                    )
                } ?: emptyList()
        }
    }

}