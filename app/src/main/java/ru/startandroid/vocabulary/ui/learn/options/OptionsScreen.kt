package ru.startandroid.vocabulary.ui.learn.options

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
    remember {
        // workaround to call this method only when screen is created
        // hope, will find better way later
        optionsScreenViewModel.onViewCreated()
        0
    }

    val chips = optionsScreenViewModel.chips.observeAsState()
    val data = optionsScreenViewModel.data.observeAsState()
    OptionsScreenInternal(
        count = optionsScreenViewModel.count,
        availableCount = data.value?.size.toString(),
        chips = chips.value ?: emptyList(),
        showDebugInfo = optionsScreenViewModel.showDebugInfo,
        onPreviewClick = {
            optionsScreenViewModel.onPreviewClick()
            navController.navigate("preview")
        },
        onChipClick = optionsScreenViewModel::onChipClick,
        onSelectAll = optionsScreenViewModel::onSelectAll,
        onResetAll = optionsScreenViewModel::onResetAll
    )
}

@Composable
private fun OptionsScreenInternal(
    count: MutableState<String> = mutableStateOf("10"),
    availableCount: String? = "",
    chips: List<ChipData> = emptyList(),
    showDebugInfo: MutableState<Boolean> = mutableStateOf(false),
    onPreviewClick: () -> Unit = { },
    onChipClick: (String) -> Unit = { },
    onSelectAll: () -> Unit = { },
    onResetAll: () -> Unit = { }
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = count.value,
            onValueChange = { count.value = it },
            label = { Text("Words count") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.width(120.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        FlowRow(mainAxisSpacing = 8.dp, crossAxisSpacing = 8.dp) {
            for (chip in chips) {
                Chip(
                    label = chip.label,
                    isSelected = chip.isSelected,
                    onClick = onChipClick
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Text(
                text = "Select All",
                color = Color.Blue,
                modifier = Modifier.clickable { onSelectAll() })
            Spacer(modifier = Modifier.weight(1f))
            Text(
                text = "Reset All",
                color = Color.Blue,
                modifier = Modifier.clickable { onResetAll() })
        }
        Spacer(modifier = Modifier.height(8.dp))
        Row {
            Checkbox(checked = showDebugInfo.value, onCheckedChange = {
                showDebugInfo.value = it
            })
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = "Show debug info", modifier = Modifier.clickable {
                showDebugInfo.value = !showDebugInfo.value
            })
        }

        Spacer(modifier = Modifier.height(16.dp))
        val availableString = availableCount?.let { "($it)" } ?: ""
        Button(onClick = { onPreviewClick() }) {
            Text(text = "Preview $availableString")
        }
    }
}

@Composable
private fun Chip(
    label: String,
    isSelected: Boolean = false,
    onClick: (String) -> Unit = { }
) {
    val border = if (isSelected)
        BorderStroke(3.dp, MaterialTheme.colors.primary)
    else
        BorderStroke(1.dp, Color.Gray)
    Surface(
        modifier = Modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) {
                onClick(label)
            },
        border = border,
        shape = RoundedCornerShape(16.dp),
    ) {
        Text(
            text = label,
            modifier = Modifier
                .padding(12.dp)
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
            availableCount = "100",
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

