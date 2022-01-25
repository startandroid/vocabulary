package ru.startandroid.vocabulary.model.dto

data class SessionOptions(
    val count: Int = 0,
    val tags: Set<String> = emptySet()
)