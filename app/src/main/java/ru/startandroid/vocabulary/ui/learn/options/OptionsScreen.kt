package ru.startandroid.vocabulary.ui.learn.options

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
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
import androidx.navigation.NavOptionsBuilder
import ru.startandroid.vocabulary.model.dto.WordData
import ru.startandroid.vocabulary.ui.theme.VocabularyTheme


@Composable
fun OptionsScreen(
    navController: NavHostController,
    optionsScreenViewModel: OptionsScreenViewModel = hiltViewModel()
) {
    val data = optionsScreenViewModel.data.observeAsState()
    OptionsScreenInternal(
        onPreviewClick = {
            optionsScreenViewModel.onPreviewClick(it)
            navController.navigate("preview")
        }
    )
}


@Composable
private fun OptionsScreenInternal(
    onPreviewClick: (Int) -> Unit = { }
) {
    var count by rememberSaveable { mutableStateOf("15") }
    Column(modifier = Modifier
        .padding(16.dp)
        .fillMaxSize()) {
        OutlinedTextField(
            value = count,
            onValueChange = { count = it },
            label = { Text("Words count") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Button(onClick = { onPreviewClick(count.toInt()) }) {
            Text(text = "Preview")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OptionsScreenPreview() {
    VocabularyTheme {
        OptionsScreenInternal()
    }
}

