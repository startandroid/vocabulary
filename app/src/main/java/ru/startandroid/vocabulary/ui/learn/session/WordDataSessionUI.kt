package ru.startandroid.vocabulary.ui.learn.session

data class WordDataSessionUI(
    val word: String,
    val translate: String,
    val tags: Set<String>
)