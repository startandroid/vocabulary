package ru.startandroid.vocabulary.model.usecase

import ru.startandroid.vocabulary.model.dto.SessionOptions
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.model.repository.WordRepository
import javax.inject.Inject
import javax.inject.Provider
import kotlin.random.Random

class GetWordsAccordingToOptionsUseCase @Inject constructor(
    private val wordRepository: WordRepository
) {

    // TODO make data extracting more fair according by all tags
    // Otherwise some tags will not be in results
    suspend fun invoke(options: SessionOptions): List<WordData> {
        return wordRepository.getAllWords()
            .filter { wordData -> wordData.tags.any { options.tags.contains(it) } }
    }

}