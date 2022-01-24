package ru.startandroid.vocabulary.ui.learn.preview

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.navOptions
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.ui.learn.options.OptionsScreenViewModel
import ru.startandroid.vocabulary.ui.theme.VocabularyTheme

@Composable
fun PreviewScreen(
    navController: NavController,
    optionsScreenViewModel: OptionsScreenViewModel = hiltViewModel(navController.getBackStackEntry("options"))
) {
    val data = optionsScreenViewModel.data.observeAsState()
    PreviewScreenInternal(
        onRefreshClick = optionsScreenViewModel::onRefresh,
        onLearnClick = { navController.navigate("session", navOptions { popUpTo("options") }) },
        data = data.value!!
    )
}

@Composable
fun PreviewScreenInternal(
    onRefreshClick: () -> Unit = { },
    onLearnClick: () -> Unit = { },
    data: List<WordData> = emptyList()
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Button(onClick = onRefreshClick) {
            Text(text = "Refresh")
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
        Button(onClick = onLearnClick) {
            Text(text = "Learn")
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