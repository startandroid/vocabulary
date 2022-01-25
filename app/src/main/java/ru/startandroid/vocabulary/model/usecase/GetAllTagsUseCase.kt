package ru.startandroid.vocabulary.model.usecase

import ru.startandroid.vocabulary.model.repository.WordRepository
import javax.inject.Inject

class GetAllTagsUseCase @Inject constructor(
    private val wordRepository: WordRepository
) {

    suspend fun invoke(): List<String> {
        return wordRepository.getAllWords()
            .flatMap { it.tags }
            .filter { it.isNotEmpty() }
            .distinct()
            .sorted()
    }

}