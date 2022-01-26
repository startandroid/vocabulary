package ru.startandroid.vocabulary.ui.learn.session

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.model.usecase.DecrementScoreUseCase
import ru.startandroid.vocabulary.model.usecase.IncrementScoreUseCase
import javax.inject.Inject

@HiltViewModel
class SessionScreenViewModel @Inject constructor(
    private val incrementScoreUseCase: IncrementScoreUseCase,
    private val decrementScoreUseCase: DecrementScoreUseCase
) : ViewModel() {

    private val words = mutableListOf<WordDataSession>()
    private val wrong = mutableListOf<WordDataSession>()
    private var counter = 0

    private var lastWords = LastWords(0)

    private val secret = "*".repeat(10)

    lateinit var currentWordDataSession: WordDataSession

    private val _currentWord = MutableLiveData<WordDataSessionUI>()
    val currentWord: LiveData<WordDataSessionUI> = _currentWord

    val debugInfo = mutableStateOf("")

    fun putWordData(data: List<WordData>) {
        if (words.isNotEmpty()) return
        words.addAll(data.map {
            WordDataSession(
                word = it.word,
                translate = it.translate,
                tags = it.tags
            )
        })
        calculateLast()
        nextWord()
    }

    private fun calculateLast() {
        val lastWordsSize = kotlin.math.min(words.size / 2, 8)
//        val lastWordsSize = when (words.size) {
//            in 0..1 -> 0
//            in 2..3 -> 1
//            in 4..5 -> 2
//            in 6..7 -> 3
//            in 8..9 -> 4
//            else -> 5
//        }
        lastWords = LastWords(lastWordsSize)
    }

    private fun nextWord() {
        val s = shouldPickWrong()
        Log.d("qweee", "nextWord should pick wrong = $s, counter = $counter")
        Log.d("qweee", "words = ${words.map { it.word to it.sessionScore }}")
        Log.d("qweee", "wrong = ${wrong.map { it.word to it.sessionScore }}")
        Log.d("qweee", "lastWords = ${lastWords.entries.map { "(${it.key.word}, ${it.key.sessionScore}, ${it.value})" }}")

        val list = if (s) {
            counter = 0
            wrong
        } else {
            words
        }
        list.shuffle()
        list.sortBy { it.sessionScore }
        counter++
        debugInfo.value = """
            [words] ${words.joinToString(", ") { it.word }}
            
            [wrong] ${wrong.joinToString(", ") { it.word }}
            
            [last] ${lastWords.entries.joinToString(", ") { it.key.word }}
        """.trimIndent()
        val wordData = list[0]
        Log.d("qweee", "nextWord = $wordData")
        currentWordDataSession = wordData
        _currentWord.value = WordDataSessionUI(
            word = if (currentWordDataSession.reverse) secret else wordData.word,
            translate = if (currentWordDataSession.reverse) wordData.translate else secret,
            tags = wordData.tags
        )
    }

    private fun shouldPickWrong(): Boolean {
        if (words.isEmpty()) return true

        if (wrong.isEmpty()) return false

        if (wrong.size >= 10) return true

        if (wrong.size >= words.size && counter >= 2) return true

        if (counter >= 3) return true

//        val count = when (wrong.size) {
//            in 1..3 -> 5
//            in 4..5 -> 4
//            in 6..7 -> 3
//            in 8..9 -> 2
//            else -> return false
//        }
//        if (counter >= count) return true

        return false
    }

    fun correct() {
        Log.d("qweee", "--- correct ---")
        val wordData = currentWordDataSession
        viewModelScope.launch {
            incrementScoreUseCase.invoke(wordData.word)
        }
        words.remove(wordData)
        wrong.remove(wordData)
        lastWords.putCorrect(
            wordData.copy(
                reverse = !wordData.reverse,
                sessionScore = wordData.sessionScore + 1
            )
        )
        nextWord()
    }

    fun wrong() {
        Log.d("qweee", "--- wrong ---")
        val wordData = currentWordDataSession
        viewModelScope.launch {
            decrementScoreUseCase.invoke(wordData.word)
        }
        words.remove(wordData)
        wrong.remove(wordData)
        lastWords.putWrong(wordData.copy(sessionScore = wordData.sessionScore - 1))
        nextWord()
    }

    fun openSecret() {
        Log.d("qweee", "open secret")
        _currentWord.value = WordDataSessionUI(
            word = currentWordDataSession.word,
            translate = currentWordDataSession.translate,
            tags = currentWordDataSession.tags
        )
    }

    inner class LastWords(private val maxSize: Int) : LinkedHashMap<WordDataSession, String>() {

        fun putCorrect(word: WordDataSession) {
            put(word, "correct")
        }

        fun putWrong(word: WordDataSession) {
            put(word, "wrong")
        }

        override fun removeEldestEntry(eldest: MutableMap.MutableEntry<WordDataSession, String>?): Boolean {
            val del = size > maxSize
            if (del && eldest != null) {
                if (eldest.value == "correct") words.add(eldest.key)
                if (eldest.value == "wrong") wrong.add(eldest.key)
            }
            return del
        }


    }

}

data class WordDataSession(
    val word: String,
    val translate: String,
    val tags: Set<String>,
    val reverse: Boolean = false,
    val sessionScore: Int = 0
)