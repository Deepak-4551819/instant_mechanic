package com.justunfold.instantmechanic.presentation.details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justunfold.instantmechanic.domain.usecase.GetMechanicDetailsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsViewModel @Inject constructor(
    private val getMechanicDetailsUseCase: GetMechanicDetailsUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val _uiState = MutableStateFlow(DetailsUiState())
    val uiState = _uiState.asStateFlow()

    init {
        savedStateHandle.get<String>("mechanicId")?.let { id ->
            loadMechanicDetails(id)
        }
    }

    private fun loadMechanicDetails(id: String) {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            val mechanic = getMechanicDetailsUseCase(id)
            if (mechanic != null) {
                _uiState.update { it.copy(isLoading = false, mechanic = mechanic) }
            } else {
                _uiState.update { it.copy(isLoading = false, errorMessage = "Mechanic not found") }
            }
        }
    }
}
