package ru.startandroid.vocabulary.data.mapper

import ru.startandroid.vocabulary.data.db.WordDataDb
import ru.startandroid.vocabulary.model.dto.WordDataNew
import javax.inject.Inject

class WordMapperNewToDb @Inject constructor() : Mapper<WordDataNew, WordDataDb>() {
    override fun map(input: WordDataNew): WordDataDb =
        WordDataDb(word = input.word, translate = input.translate, tags = input.tags.joinToString("|"), score = 0)

}