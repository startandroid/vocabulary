package ru.startandroid.vocabulary.model.dto

data class WordDataNew(
    val word: String,
    val translate: String,
    val tags: Set<String>
)