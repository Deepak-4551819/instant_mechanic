package com.justunfold.instantmechanic

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.justunfold.instantmechanic.data.repository.FirebaseRepository
import com.justunfold.instantmechanic.presentation.auth.AuthScreen
import com.justunfold.instantmechanic.presentation.bookings.BookingsScreen
import com.justunfold.instantmechanic.presentation.details.DetailsScreen
import com.justunfold.instantmechanic.presentation.home.HomeScreen
import com.justunfold.instantmechanic.presentation.navigation.BottomNavItem
import com.justunfold.instantmechanic.presentation.navigation.Screen
import com.justunfold.instantmechanic.presentation.profile.ProfileScreen
import com.justunfold.instantmechanic.presentation.request.RequestServiceScreen
import com.justunfold.instantmechanic.presentation.theme.InstantMechanicTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject lateinit var firebaseRepository: FirebaseRepository
    @Inject lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            window.isNavigationBarContrastEnforced = false
        }

        setContent {
            InstantMechanicTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentRoute = navBackStackEntry?.destination?.route

                val bottomBarRoutes = listOf(
                    BottomNavItem.Garages.route,
                    BottomNavItem.Bookings.route,
                    BottomNavItem.Profile.route
                )
                val shouldShowBottomBar = currentRoute in bottomBarRoutes

                val startDestination = if (auth.currentUser != null) {
                    BottomNavItem.Garages.route
                } else {
                    Screen.Auth.route
                }

                Scaffold(
                    contentWindowInsets = WindowInsets(0, 0, 0, 0),
                    bottomBar = {
                        if (shouldShowBottomBar) {
                            NavigationBar(
                                containerColor = MaterialTheme.colorScheme.surfaceContainerLowest,
                                tonalElevation = 6.dp
                            ) {
                                val items = listOf(
                                    BottomNavItem.Garages,
                                    BottomNavItem.Bookings,
                                    BottomNavItem.Profile
                                )
                                items.forEach { item ->
                                    NavigationBarItem(
                                        icon = { Icon(item.icon, contentDescription = item.title) },
                                        label = {
                                            Text(
                                                text = item.title,
                                                style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold)
                                            )
                                        },
                                        selected = currentRoute == item.route,
                                        onClick = {
                                            navController.navigate(item.route) {
                                                popUpTo(navController.graph.findStartDestination().id) {
                                                    saveState = true
                                                }
                                                launchSingleTop = true
                                                restoreState = true
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
                    ) {
                        composable(Screen.Auth.route) {
                            AuthScreen(
                                auth = auth,
                                onAuthSuccess = {
                                    navController.navigate(BottomNavItem.Garages.route) {
                                        popUpTo(Screen.Auth.route) { inclusive = true }
                                    }
                                }
                            )
                        }

                        composable(BottomNavItem.Garages.route) {
                            HomeScreen(
                                onMechanicClick = { mechanicId ->
                                    navController.navigate(Screen.Details.passId(mechanicId))
                                }
                            )
                        }

                        composable(BottomNavItem.Bookings.route) {
                            BookingsScreen(firebaseRepository = firebaseRepository)
                        }

                        composable(BottomNavItem.Profile.route) {
                            ProfileScreen(
                                auth = auth,
                                onSignOut = {
                                    navController.navigate(Screen.Auth.route) {
                                        popUpTo(0) { inclusive = true }
                                    }
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
                            val mechId = backStackEntry.arguments?.getString("mechanicId") ?: ""
                            val mechName = backStackEntry.arguments?.getString("mechanicName") ?: "Garage"

                            RequestServiceScreen(
                                mechanicId = mechId,
                                mechanicName = mechName,
                                onBackClick = { navController.popBackStack() },
                                onNavigateToBookings = {
                                    navController.navigate(BottomNavItem.Bookings.route) {
                                        popUpTo(BottomNavItem.Garages.route) {
                                            inclusive = false
                                        }
                                        launchSingleTop = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
