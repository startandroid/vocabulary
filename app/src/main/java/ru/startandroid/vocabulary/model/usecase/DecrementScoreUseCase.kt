package ru.startandroid.vocabulary.model.usecase

import ru.startandroid.vocabulary.model.repository.WordRepository
import javax.inject.Inject

class DecrementScoreUseCase @Inject constructor(
    private val wordRepository: WordRepository
) {

    suspend fun invoke(word: String) {
        wordRepository.decrementScore(word)
    }

}