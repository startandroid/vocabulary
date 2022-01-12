package ru.startandroid.vocabulary.model.usecase

import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.model.repository.WordRepository
import javax.inject.Inject
import javax.inject.Provider
import kotlin.random.Random

class GetWordsAccordingToOptionsUseCase @Inject constructor(
    private val wordRepository: WordRepository,
    private val randomProvider: Provider<Random>
) {

    suspend fun invoke(count: Int): List<WordData> {
        return wordRepository.getAllWords()
            .shuffled(randomProvider.get())
            .take(count)
    }

}