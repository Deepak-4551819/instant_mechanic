package com.justunfold.instantmechanic.presentation.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Person
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    data object Garages : BottomNavItem("garages_tab", "Garages", Icons.Default.Build)
    data object Bookings : BottomNavItem("bookings_tab", "Bookings", Icons.AutoMirrored.Filled.FormatListBulleted)
    data object Profile : BottomNavItem("profile_tab", "Profile", Icons.Default.Person)
}
