package ru.startandroid.vocabulary.model.usecase

import android.net.Uri
import ru.startandroid.vocabulary.model.dto.WordDataNew
import ru.startandroid.vocabulary.model.repository.FileRepository
import javax.inject.Inject

class ImportWordsFromFileUseCase @Inject constructor(
    private val fileRepository: FileRepository
) {

    suspend fun invoke(uri: Uri): List<WordDataNew> =
        fileRepository.importWordsFromFile(uri)

}