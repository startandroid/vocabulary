package ru.startandroid.vocabulary.model.dto

data class WordDataFile(
    val word: String,
    val translate: String,
    val tags: Set<String>
)