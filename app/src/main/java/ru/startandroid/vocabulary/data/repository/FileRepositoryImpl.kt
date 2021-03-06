package ru.startandroid.vocabulary.data.repository

import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import ru.startandroid.vocabulary.data.ResourceProvider
import ru.startandroid.vocabulary.model.dto.WordDataNew
import ru.startandroid.vocabulary.model.repository.FileRepository
import javax.inject.Provider
import javax.inject.Singleton
import kotlin.random.Random

@Singleton
class FileRepositoryImpl(
    private val resourceProvider: ResourceProvider,
    private val randomProvider: Provider<Random>
) : FileRepository {
    override suspend fun importWordsFromFile(uri: Uri): List<WordDataNew> {
        return withContext(Dispatchers.IO) {
            readDataFromFile(uri)
            //generateFakeData()
        }
    }

    private fun readDataFromFile(uri: Uri): List<WordDataNew> {
        // TODO handle errors
        return resourceProvider.openInputStream(uri)
            ?.readBytes()
            ?.toString(Charsets.UTF_8)
            ?.split("\n")
            ?.mapNotNull {
                val data = it.split("###")
                if (data.size == 3)
                    WordDataNew(
                        word = data[0].trim(),
                        translate = data[1].trim(),
                        tags = data[2].split(",")
                            .map { it.lowercase().trim() }
                            .filter { it.isNotEmpty() }
                            .toSet()
                    )
                else
                    null
            } ?: emptyList()
    }

    private fun generateFakeData(): List<WordDataNew> {
        val random = randomProvider.get()
        return List(1000) {
            val tags = List(random.nextInt(5)) {
                "tag${random.nextInt(1,20)}"
            }.toSet()
            WordDataNew(word = "Word $it", translate = "Translate $it", tags = tags)
        }
    }

}