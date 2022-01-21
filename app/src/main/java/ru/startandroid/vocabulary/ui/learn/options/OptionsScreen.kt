package ru.startandroid.vocabulary.ui.learn.options

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.ui.theme.VocabularyTheme


@Composable
fun OptionsScreen(
    navController: NavHostController,
    optionsScreenViewModel: OptionsScreenViewModel = hiltViewModel()
) {
    val data = optionsScreenViewModel.data.observeAsState()
    OptionsScreenInternal(
        onPreviewClick = optionsScreenViewModel::onPreviewClick,
        onLearnClick = { navController.navigate("session") },
        data = data.value!!
    )
}


@Composable
private fun OptionsScreenInternal(
    onPreviewClick: (Int) -> Unit = { },
    onLearnClick: () -> Unit = { },
    data: List<WordData> = emptyList()
) {
    var count by rememberSaveable { mutableStateOf("15") }
    Column(modifier = Modifier.padding(16.dp).fillMaxSize()) {
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
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(data.size) { index ->
                val wordData = data[index]
                Text(
                    text = "${wordData.word} - ${wordData.translate}",
                    style = MaterialTheme.typography.body1
                )

            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        if (data.isNotEmpty()) {
            Button(onClick = { onLearnClick() }) {
                Text(text = "Learn")
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

