package ru.startandroid.vocabulary.data.repository

import ru.startandroid.vocabulary.data.db.WordsDao
import ru.startandroid.vocabulary.data.mapper.WordMapperDbToUi
import ru.startandroid.vocabulary.data.mapper.WordMapperNewToDb
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.model.dto.WordDataNew
import ru.startandroid.vocabulary.model.repository.WordRepository

class WordRepositoryImpl(
    private val wordsDao: WordsDao,
    private val wordMapperNewToDb: WordMapperNewToDb,
    private val wordMapperDbToUi: WordMapperDbToUi
): WordRepository  {

    override suspend fun addNewWords(words: List<WordDataNew>) {
        wordsDao.insertAll(wordMapperNewToDb.mapList(words))
    }

    override suspend fun getAllWords(): List<WordData> {
        return wordMapperDbToUi.mapList(wordsDao.getAll())
    }

    override suspend fun incrementScore(word: String) {
        wordsDao.incrementScore(word)
    }

    override suspend fun decrementScore(word: String) {
        wordsDao.decrementScore(word)
    }

    override suspend fun learned(word: String) {
        wordsDao.setLastLearnedTime(word, System.currentTimeMillis())
    }

}