package ru.startandroid.vocabulary.ui.learn.preview

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import ru.startandroid.vocabulary.model.dto.WordData

class PreviewScreenViewModel : ViewModel() {

    private var wordDataList: List<WordData> = emptyList()
    private var requiredCount = 0

    val sliderState = mutableStateOf(SliderState())

    var chosenWordDataList: MutableState<List<WordData>> = mutableStateOf(emptyList())

    fun putData(data: List<WordData>, count: Int) {
        if (wordDataList.isNotEmpty()) return
        wordDataList = data.toList()
        requiredCount = count

        initSliderState()

        chooseWordsData()
    }

    fun onRefresh() {
        chooseWordsData()
    }

    private fun chooseWordsData() {
        if (wordDataList.size <= requiredCount) {
            chosenWordDataList.value = wordDataList.toList()
            return
        }

        val (newData, oldData) = wordDataList.partition { it.lastLearned == 0L }

        if (oldData.isEmpty()) {
            chosenWordDataList.value = wordDataList.shuffled().take(requiredCount)
            return
        }

        val newCount = if (sliderState.value.enabled) sliderState.value.value else 0
        val oldCount =
            if (sliderState.value.enabled) sliderState.value.amount - sliderState.value.value else requiredCount

        val newChosenWords = newData.shuffled().take(newCount)
        val oldChosenWords = GetOldWordsUseCase().invoke(oldData, oldCount)
        chosenWordDataList.value = newChosenWords + oldChosenWords
    }


    private fun initSliderState() {

        if (wordDataList.size <= requiredCount) return

        val (newData, oldData) = wordDataList.partition { it.lastLearned == 0L }

        if (newData.isEmpty()) return

        if (oldData.isEmpty()) return

        val minValue = (requiredCount - oldData.size).coerceAtLeast(0)
        val maxValue = newData.size.coerceAtMost(requiredCount)

        sliderState.value = SliderState(
            enabled = true,
            valueRange = minValue.toFloat()..maxValue.toFloat(),
            steps = maxValue - minValue - 1,
            value = minValue,
            amount = requiredCount
        )
    }

    fun onSliderValueChanged(value: Int) {
        sliderState.value = sliderState.value.copy(value = value)
    }

}

class GetOldWordsUseCase {

    var firstSegmentWords = mutableListOf<WordData>()
    var secondSegmentWords = mutableListOf<WordData>()
    var thirdSegmentWords = mutableListOf<WordData>()

    fun invoke(wordDataList: List<WordData>, count: Int): List<WordData> {
        val thirdSegmentCount = count / 10
        val secondSegmentCount = count / 5
        val firstSegmentCount = count - secondSegmentCount - thirdSegmentCount

        firstSegmentWords = wordDataList.filter { it.score < 1 }.toMutableList()
        secondSegmentWords = wordDataList.filter { it.score in 1..10 }.toMutableList()
        thirdSegmentWords = wordDataList.filter { it.score > 10 }.toMutableList()

        val firstSegmentChosenWords = getFromSegments(
            firstSegmentCount,
            listOf(::getFromFirstSegment, ::getFromSecondSegment, ::getFromThirdSegment)
        )
        val secondSegmentChosenWords = getFromSegments(
            secondSegmentCount,
            listOf(::getFromSecondSegment, ::getFromFirstSegment, ::getFromThirdSegment)
        )
        val thirdSegmentChosenWords = getFromSegments(
            thirdSegmentCount,
            listOf(::getFromThirdSegment, ::getFromFirstSegment, ::getFromSecondSegment)
        )

        return firstSegmentChosenWords + secondSegmentChosenWords + thirdSegmentChosenWords
    }

    private fun getFromSegments(
        neededCount: Int,
        getFuncs: List<(Int) -> List<WordData>>
    ): List<WordData> {
        var count = neededCount
        val totalResult = mutableListOf<WordData>()
        for (func in getFuncs) {
            val result = func(count)
            totalResult.addAll(result)
            count -= result.size
            if (count <= 0) break
        }
        return totalResult
    }

    private fun getFromFirstSegment(count: Int): List<WordData> =
        getFromSegment(firstSegmentWords, count) {
            sortedBy { it.score }
        }

    private fun getFromSecondSegment(count: Int): List<WordData> =
        getFromSegment(secondSegmentWords, count)

    private fun getFromThirdSegment(count: Int): List<WordData> =
        getFromSegment(thirdSegmentWords, count) {
            sortedBy { it.lastLearned }
        }

    private fun getFromSegment(
        data: MutableList<WordData>,
        count: Int,
        func: List<WordData>.() -> List<WordData> = { this }
    ): List<WordData> {
        val result = mutableListOf<WordData>()
        result.addAll(data.shuffled().let { it.func() }.take(count))
        data.removeAll(result)
        return result
    }

}

data class SliderState(
    val enabled: Boolean = false,
    val valueRange: ClosedFloatingPointRange<Float> = 0f..1f,
    val steps: Int = 0,
    val value: Int = 0,
    val amount: Int = 0
)