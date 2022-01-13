package ru.startandroid.vocabulary.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController

@Composable
fun HomeScreen(navController: NavHostController) {
    HomeScreenInternal(
        onImportClick = { navController.navigate("import") },
        onLearnClick = { navController.navigate("options") }
    )
}

@Composable
fun HomeScreenInternal(
    onImportClick: () -> Unit = {},
    onLearnClick: () -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment =  Alignment.Center) {
        Column {
            Button(onClick = onImportClick) {
                Text(text = "Import")
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = onLearnClick) {
                Text(text = "Learn")
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreenInternal()
}

