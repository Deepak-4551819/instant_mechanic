package com.justunfold.instantmechanic.presentation.navigation

sealed class Screen(val route: String) {
    data object Home : Screen("home_screen")
    data object Details : Screen("details_screen/{mechanicId}") {
        fun passId(id: String) = "details_screen/$id"
    }
    data object RequestService : Screen("request_service_screen/{mechanicId}/{mechanicName}") {
        fun passDetails(id: String, name: String) = "request_service_screen/$id/$name"
    }
}
