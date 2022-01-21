package ru.startandroid.vocabulary.ui.importdata

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import ru.startandroid.vocabulary.model.dto.WordDataNew
import ru.startandroid.vocabulary.ui.theme.VocabularyTheme

@Composable
fun ImportScreen(
    importScreenViewModel: ImportScreenViewModel = hiltViewModel()
) {
    val data = importScreenViewModel.data.observeAsState()
    ImportScreenInternal(
        onFileChosen = importScreenViewModel::onFileChosen,
        onSubmitClick = importScreenViewModel::onSubmitClick,
        data = data.value!!
    )
}

@Composable
private fun ImportScreenInternal(
    onFileChosen: (uri: Uri?) -> Unit = {},
    onSubmitClick: () -> Unit = {},
    data: List<WordDataNew> = emptyList()
) {
    val filePickLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            onFileChosen(uri)
        }

    if (data.isNullOrEmpty())
        PickFileButton { filePickLauncher.launch("text/*") }
    else {
        PreviewDataAndSubmit(data, onSubmitClick)
    }
}

@Composable
fun PickFileButton(onPickClick: () -> Unit = { }) {
    Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
        Button(onClick = onPickClick) {
            Text(text = "Pick file")
        }
    }
}

@Composable
fun PreviewDataAndSubmit(
    data: List<WordDataNew>,
    onSubmitClick: () -> Unit = { }
) {
    Column(modifier = Modifier.padding(16.dp)) {
        Button(onClick = onSubmitClick) {
            Text(text = "Submit")
        }
        LazyColumn {
            items(data.size) { index ->
                val row = data[index]
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(
                        text = "${row.word} - ${row.translate}",
                        style = MaterialTheme.typography.body1
                    )
                    Text(text = "${row.tags}", style = MaterialTheme.typography.body2)
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun PickFilePreview() {
    VocabularyTheme {
        ImportScreenInternal()
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun DataAndSubmitPreview() {
    val data = remember {
        listOf(
            WordDataNew(
                word = "Word 1",
                translate = "Translate 1",
                tags = setOf("tag1", "tag2", "tag3")
            ),
            WordDataNew(
                word = "Word 2",
                translate = "Translate 2",
                tags = setOf("tag4", "tag5", "tag6")
            )
        )
    }
    VocabularyTheme {
        ImportScreenInternal(data = data)
    }
}