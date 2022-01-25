package ru.startandroid.vocabulary.ui.learn.options

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.google.accompanist.flowlayout.FlowRow
import ru.startandroid.vocabulary.ui.theme.VocabularyTheme


@Composable
fun OptionsScreen(
    navController: NavHostController,
    optionsScreenViewModel: OptionsScreenViewModel = hiltViewModel()
) {
    val chips = optionsScreenViewModel.chips.observeAsState()
    OptionsScreenInternal(
        count = optionsScreenViewModel.count,
        chips = chips.value ?: emptyList(),
        onPreviewClick = {
            optionsScreenViewModel.onPreviewClick()
            navController.navigate("preview")
        },
        onChipClick = optionsScreenViewModel::onChipClick
    )
}


@Composable
private fun OptionsScreenInternal(
    count: MutableState<Int> = mutableStateOf(10),
    chips: List<ChipData> = emptyList(),
    onPreviewClick: () -> Unit = { },
    onChipClick: (String) -> Unit = { }
) {

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = count.value.toString(),
            onValueChange = { count.value = it.toInt() },
            label = { Text("Words count") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        Spacer(modifier = Modifier.height(8.dp))
        FlowRow {
            for (chip in chips) {
                Chip(
                    label = chip.label,
                    isSelected = chip.isSelected,
                    onClick = onChipClick
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { onPreviewClick() }) {
            Text(text = "Preview")
        }
    }
}

@Composable
private fun Chip(
    label: String,
    isSelected: Boolean = false,
    onClick: (String) -> Unit = { }
) {
    Surface(
        modifier = Modifier
            .padding(4.dp)
            .border(width = 1.dp, color = Color.Black, shape = RoundedCornerShape(4.dp))
            .clickable { onClick(label) },
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colors.primary else Color.Transparent
    ) {
        Text(
            text = label,
            modifier = Modifier
                .padding(8.dp)
                .defaultMinSize(minWidth = 48.dp),
            textAlign = TextAlign.Center
        )
    }

}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun OptionsScreenPreview() {
    VocabularyTheme {
        OptionsScreenInternal(
            chips = listOf(
                ChipData("Chip1", true),
                ChipData("Chip123", false),
                ChipData("Chip23366", false),
                ChipData("C2", true),
                ChipData("Chip3232", true),
                ChipData("Chip4223232", false),
            )
        )
    }
}

