package ru.startandroid.vocabulary.data.repository

import ru.startandroid.vocabulary.data.db.WordsDao
import ru.startandroid.vocabulary.data.mapper.WordMapperNewToDb
import ru.startandroid.vocabulary.model.dto.WordDataNew
import ru.startandroid.vocabulary.model.repository.WordRepository

class WordRepositoryImpl(
    private val wordsDao: WordsDao,
    private val wordMapperNewToDb: WordMapperNewToDb
): WordRepository  {

    override suspend fun addNewWords(words: List<WordDataNew>) {
        wordsDao.insertAll(wordMapperNewToDb.mapList(words))
    }

}