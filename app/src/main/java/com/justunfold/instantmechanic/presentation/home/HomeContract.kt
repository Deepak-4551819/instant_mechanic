package com.justunfold.instantmechanic.presentation.home

import com.justunfold.instantmechanic.domain.model.Mechanic

// Immutable State
data class HomeUiState(
    val isLoading: Boolean = false,
    val isRefreshing: Boolean = false,
    val mechanics: List<Mechanic> = emptyList(),
    val searchQuery: String = "",
    val filterOnlyOpen: Boolean = false,
    val errorMessage: String? = null
) {
    val filteredMechanics: List<Mechanic>
        get() = mechanics.filter { mechanic ->
            val matchesQuery = mechanic.name.contains(searchQuery, ignoreCase = true) ||
                    mechanic.location.contains(searchQuery, ignoreCase = true) ||
                    mechanic.services.any { it.contains(searchQuery, ignoreCase = true) }
            val matchesOpen = if (filterOnlyOpen) mechanic.isOpen else true
            matchesQuery && matchesOpen
        }
}

// User Actions / Intents
sealed interface HomeIntent {
    data object LoadMechanics : HomeIntent
    data object RefreshMechanics : HomeIntent
    data class SearchQueryChanged(val query: String) : HomeIntent
    data class ToggleOpenFilter(val onlyOpen: Boolean) : HomeIntent
}

// One-shot Side Effects (Snackbars, Navigations)
sealed interface HomeSideEffect {
    data class ShowSnackbar(val message: String) : HomeSideEffect
}
