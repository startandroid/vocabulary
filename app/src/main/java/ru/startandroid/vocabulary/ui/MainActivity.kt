package ru.startandroid.vocabulary.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import dagger.hilt.android.AndroidEntryPoint
import ru.startandroid.vocabulary.ui.home.HomeScreen
import ru.startandroid.vocabulary.ui.importdata.ImportScreen
import ru.startandroid.vocabulary.ui.learn.options.OptionsScreen
import ru.startandroid.vocabulary.ui.learn.session.SessionScreen
import ru.startandroid.vocabulary.ui.theme.VocabularyTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            VocabularyTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    MainNavigation(navController)
                }
            }
        }
    }
}

@Composable
fun MainNavigation(navController: NavHostController) {
    // TODO create a nested navigation?
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            HomeScreen(navController)
        }
        composable("import") {
            ImportScreen()
        }
        composable("options") {
            OptionsScreen(navController)
        }
        composable("session") {
            SessionScreen(navController)
        }
    }
}