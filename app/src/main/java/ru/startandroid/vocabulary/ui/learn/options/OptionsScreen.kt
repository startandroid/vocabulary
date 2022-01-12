package ru.startandroid.vocabulary.ui.learn.options

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.ui.theme.VocabularyTheme


@Composable
fun OptionsScreen(
    optionsScreenViewModel: OptionsScreenViewModel = viewModel()
) {
    val data = optionsScreenViewModel.data.observeAsState()
    OptionsScreenInternal(
        onPreviewClick = optionsScreenViewModel::onPreviewClick,
        data = data.value!!
    )
}


@Composable
private fun OptionsScreenInternal(
    onPreviewClick: (Int) -> Unit = { },
    data: List<WordData> = emptyList()
) {
    var count by rememberSaveable { mutableStateOf("10") }
    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = count,
            onValueChange = {count = it},
            label = { Text("Words count") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onPreviewClick(count.toInt()) }) {
            Text(text = "Preview")
        }
        Spacer(modifier = Modifier.height(8.dp))
        LazyColumn {
            items(data.size) { index ->
                val wordData = data[index]
                Text(
                    text = "${wordData.word} - ${wordData.translate}",
                    style = MaterialTheme.typography.body1
                )

            }
        }
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OptionsScreenPreview() {
    VocabularyTheme {
        OptionsScreenInternal(
            data = listOf(
                WordData(
                    word = "Word 1",
                    translate = "Translate 1",
                    tags = setOf("tag1", "tag2", "tag3")
                ),
                WordData(
                    word = "Word 2",
                    translate = "Translate 2",
                    tags = setOf("tag4", "tag5", "tag6")
                )
            )
        )
    }
}

