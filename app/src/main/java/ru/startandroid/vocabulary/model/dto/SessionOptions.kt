package ru.startandroid.vocabulary.model.dto

import kotlinx.serialization.Serializable

@Serializable
data class SessionOptions(
    val count: Int = 0,
    val tags: Set<String> = emptySet()
)