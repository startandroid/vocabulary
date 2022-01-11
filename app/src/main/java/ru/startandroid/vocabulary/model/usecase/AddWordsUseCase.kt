package ru.startandroid.vocabulary.model.usecase

import ru.startandroid.vocabulary.model.dto.WordDataNew
import ru.startandroid.vocabulary.model.repository.WordRepository
import javax.inject.Inject

class AddWordsUseCase @Inject constructor(
    private val wordRepository: WordRepository
) {

    suspend fun invoke(newWords: List<WordDataNew>) {
        wordRepository.addNewWords(newWords)
    }

}