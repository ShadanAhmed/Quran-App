package com.example.quran.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.quran.presentation.home_screen.HomeScreen
import com.example.quran.presentation.util.Screen
import com.example.quran.presentation.verse_screen.VerseScreen
import com.example.quran.ui.theme.QuranTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            QuranTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = Screen.HomeScreen.route
                    ) {
                        composable(route = Screen.HomeScreen.route) {
                            HomeScreen(navController)
                        }
                        composable(
                            route = Screen.VerseScreen.route +
                                    "/{chapterId}/{reciterId}/{chapterName}",
                            arguments = listOf(
                                navArgument(
                                    name = "chapterId"
                                ) {
                                    type = NavType.IntType
                                },
                                navArgument(
                                    name = "reciterId"
                                ) {
                                    type = NavType.IntType
                                },
                                navArgument(
                                    name = "chapterName"
                                ) {
                                    type = NavType.StringType
                                }
                            )
                        ) {
                            VerseScreen(navController = navController, chapterName = it.arguments!!.getString("chapterName")!!)
                        }
                    }
                }
            }
        }
    }
}