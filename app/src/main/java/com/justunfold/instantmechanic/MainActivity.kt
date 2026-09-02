package com.justunfold.instantmechanic

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.justunfold.instantmechanic.presentation.details.DetailsScreen
import com.justunfold.instantmechanic.presentation.home.HomeScreen
import com.justunfold.instantmechanic.presentation.navigation.Screen
import com.justunfold.instantmechanic.presentation.request.RequestServiceScreen
import com.justunfold.instantmechanic.presentation.theme.InstantMechanicTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            InstantMechanicTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()

                    NavHost(
                        navController = navController,
                        startDestination = Screen.Home.route
                    ) {
                        composable(Screen.Home.route) {
                            HomeScreen(
                                onMechanicClick = { mechanicId ->
                                    navController.navigate(Screen.Details.passId(mechanicId))
                                }
                            )
                        }

                        composable(
                            route = Screen.Details.route,
                            arguments = listOf(navArgument("mechanicId") { type = NavType.StringType })
                        ) {
                            DetailsScreen(
                                onBackClick = { navController.popBackStack() },
                                onRequestServiceClick = { id, name ->
                                    navController.navigate(Screen.RequestService.passDetails(id, name))
                                }
                            )
                        }

                        composable(
                            route = Screen.RequestService.route,
                            arguments = listOf(
                                navArgument("mechanicId") { type = NavType.StringType },
                                navArgument("mechanicName") { type = NavType.StringType }
                            )
                        ) { backStackEntry ->
                            val mechanicName = backStackEntry.arguments?.getString("mechanicName") ?: "Garage"
                            RequestServiceScreen(
                                mechanicName = mechanicName,
                                onBackClick = { navController.popBackStack() }
                            )
                        }
                    }
                }
            }
        }
    }
}
