package ru.startandroid.vocabulary.model.dto

data class WordData(
    val word: String,
    val translate: String,
    val tags: Set<String>,
    val score: Int,
    val lastLearned: Long
)