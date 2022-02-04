package ru.startandroid.vocabulary.data.mapper

import ru.startandroid.vocabulary.data.db.WordDataDb
import ru.startandroid.vocabulary.model.dto.WordData
import javax.inject.Inject

class WordMapperDbToUi @Inject constructor() : Mapper<WordDataDb, WordData>() {
    override fun map(input: WordDataDb): WordData =
        WordData(
            word = input.word,
            translate = input.translate,
            tags = input.tags.split("|").toHashSet(),
            score = input.score,
            lastLearned = input.lastLearned
        )

}