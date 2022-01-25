package ru.startandroid.vocabulary.ui.learn.session

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ru.startandroid.vocabulary.ui.learn.options.OptionsScreenViewModel

@Composable
fun SessionScreen(
    navController: NavHostController,
    optionsScreenViewModel: OptionsScreenViewModel = hiltViewModel(navController.getBackStackEntry("options")),
    sessionScreenViewModel: SessionScreenViewModel = hiltViewModel()
) {
    sessionScreenViewModel.putWordData(optionsScreenViewModel.data.value!!)
    SessionScreenAfterInit(sessionScreenViewModel)
}

@Composable
fun SessionScreenAfterInit(
    sessionScreenViewModel: SessionScreenViewModel = hiltViewModel()
) {
    val word = sessionScreenViewModel.currentWord.observeAsState()

    SessionScreenImpl(
        wordData = word.value!!,
        onOpenSecretClick = sessionScreenViewModel::openSecret,
        onCorrectClick = sessionScreenViewModel::correct,
        onWrongClick = sessionScreenViewModel::wrong
    )
}

@Composable
fun SessionScreenImpl(
    wordData: WordDataSessionUI,
    onOpenSecretClick: () -> Unit = {},
    onCorrectClick: () -> Unit = {},
    onWrongClick: () -> Unit = {}
) {

    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {

        Spacer(modifier = Modifier.height(16.dp))
        Item("Word", wordData.word, onOpenSecretClick)

        Spacer(modifier = Modifier.height(16.dp))
        Item("Translation", wordData.translate, onOpenSecretClick)

        Spacer(modifier = Modifier.height(16.dp))
        Text(text = "Tags: ${wordData.tags.joinToString(", ")}")

        Spacer(modifier = Modifier.height(32.dp))

        Row {
            Button(onClick = onCorrectClick) {
                Text(text = "Correct")
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(onClick = onWrongClick) {
                Text(text = "Wrong")
            }
        }


    }
}

@Composable
fun Item(label: String, text: String, onOpenSecretClick: () -> Unit) {

    Text(
        text = label,
        style = MaterialTheme.typography.subtitle1,
    )
    Text(
        text = AnnotatedString(text),
        style = MaterialTheme.typography.h5,
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
            .padding(16.dp)
            .clickable(true) {
                onOpenSecretClick()
            }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SessionScreenWordPreview() {
    SessionScreenImpl(
        wordData = WordDataSessionUI(
            word = "Word",
            translate = "**********",
            tags = setOf("tag1")
        )
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun SessionScreenWordReversePreview() {
    SessionScreenImpl(
        wordData = WordDataSessionUI(
            word = "**********",
            translate = "Translate",
            tags = setOf("tag2")
        )
    )
}