package ru.startandroid.vocabulary.ui.learn.preview

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.ui.learn.options.OptionsScreenViewModel
import ru.startandroid.vocabulary.ui.theme.VocabularyTheme
import java.util.*
import kotlin.math.roundToInt

@Composable
fun PreviewScreen(
    navController: NavController,
    optionsScreenViewModel: OptionsScreenViewModel = hiltViewModel(navController.getBackStackEntry("options")),
    previewScreenViewModel: PreviewScreenViewModel = hiltViewModel()
) {
    previewScreenViewModel.putData(
        optionsScreenViewModel.data.value!!,
        optionsScreenViewModel.count.value.toIntOrNull() ?: 0
    )
    val data = previewScreenViewModel.chosenWordDataList

    PreviewScreenInternal(
        data = data.value,
        showDebugInfo = optionsScreenViewModel.showDebugInfo.value,
        sliderState = previewScreenViewModel.sliderState.value,
        onSliderValueChange = previewScreenViewModel::onSliderValueChanged,
        onRefreshClick = previewScreenViewModel::onRefresh,
        onLearnClick = {
            optionsScreenViewModel.chosenData(data.value)
            navController.navigate("session", navOptions { popUpTo("options") })
        },
    )
}

@Composable
fun PreviewScreenInternal(
    data: List<WordData> = emptyList(),
    showDebugInfo: Boolean = false,
    sliderState: SliderState = SliderState(),
    onSliderValueChange: (Int) -> Unit = { },
    onRefreshClick: () -> Unit = { },
    onLearnClick: () -> Unit = { }
    ) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {

        if (sliderState.enabled) {
            Slider(value = sliderState.value.toFloat(), onValueChange = {
                Log.d("qweee", "changed $it")
                onSliderValueChange(it.roundToInt())
            }, valueRange = sliderState.valueRange, steps = sliderState.steps)
            Text(text = "Total: ${sliderState.amount}, New: ${sliderState.value}, Old: ${sliderState.amount - sliderState.value}")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = onRefreshClick) {
            Text(text = "Refresh")
        }

        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(data.size) { index ->
                val wordData = data[index]
                val date = if (wordData.lastLearned == 0L) "" else {
                    val calendar =
                        Calendar.getInstance().apply { timeInMillis = wordData.lastLearned }
                    " , ${calendar.get(Calendar.DAY_OF_MONTH)}.${calendar.get(Calendar.MONTH) + 1}.${
                        calendar.get(
                            Calendar.YEAR
                        )
                    }}"
                }
                val debug = if (showDebugInfo) "(${wordData.score}$date)" else ""
                Text(
                    text = "${wordData.word} - ${wordData.translate} $debug",
                    style = MaterialTheme.typography.body1
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        if (data.isNotEmpty()) {
            Button(onClick = onLearnClick) {
                Text(text = "Learn")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PreviewScreenPreview() {
    VocabularyTheme {
        PreviewScreenInternal(
            data = listOf(
                WordData(
                    word = "Word 1",
                    translate = "Translate 1",
                    tags = setOf("tag1", "tag2", "tag3"),
                    score = -5,
                    lastLearned = Calendar.getInstance().timeInMillis
                ),
                WordData(
                    word = "Word 2",
                    translate = "Translate 2",
                    tags = setOf("tag4", "tag5", "tag6"),
                    score = 4,
                    lastLearned = Calendar.getInstance().timeInMillis + 1000 * 60 * 60 * 24 * 10
                )
            ),
            sliderState = SliderState(
                enabled = true,
                valueRange = 10f..20f,
                steps = 9,
                value = 15,
                amount = 25
            )
        )

    }
}