package com.justunfold.instantmechanic.presentation.details

import com.justunfold.instantmechanic.domain.model.Mechanic

data class DetailsUiState(
    val isLoading: Boolean = true,
    val mechanic: Mechanic? = null,
    val errorMessage: String? = null
)
