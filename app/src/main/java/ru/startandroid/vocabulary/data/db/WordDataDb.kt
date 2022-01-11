package ru.startandroid.vocabulary.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "words")
data class WordDataDb(
    @PrimaryKey
    var word: String,
    var translate: String,
    var tags: String
)