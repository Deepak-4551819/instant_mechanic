package com.justunfold.instantmechanic.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justunfold.instantmechanic.core.util.Resource
import com.justunfold.instantmechanic.domain.usecase.GetMechanicsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMechanicsUseCase: GetMechanicsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState = _uiState.asStateFlow()

    private val _sideEffect = Channel<HomeSideEffect>(Channel.BUFFERED)
    val sideEffect = _sideEffect.receiveAsFlow()

    init {
        handleIntent(HomeIntent.LoadMechanics)
    }

    fun handleIntent(intent: HomeIntent) {
        when (intent) {
            is HomeIntent.LoadMechanics -> fetchMechanics(fetchFromRemote = false)
            is HomeIntent.RefreshMechanics -> fetchMechanics(fetchFromRemote = true)
            is HomeIntent.SearchQueryChanged -> _uiState.update { it.copy(searchQuery = intent.query) }
            is HomeIntent.ToggleOpenFilter -> _uiState.update { it.copy(filterOnlyOpen = intent.onlyOpen) }
        }
    }

    private fun fetchMechanics(fetchFromRemote: Boolean) {
        viewModelScope.launch {
            getMechanicsUseCase(fetchFromRemote).collect { result ->
                when (result) {
                    is Resource.Loading -> {
                        _uiState.update {
                            if (fetchFromRemote) it.copy(isRefreshing = result.isLoading)
                            else it.copy(isLoading = result.isLoading)
                        }
                    }
                    is Resource.Success -> {
                        _uiState.update {
                            it.copy(
                                mechanics = result.data ?: emptyList(),
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = null
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update {
                            it.copy(
                                mechanics = result.data ?: it.mechanics,
                                isLoading = false,
                                isRefreshing = false,
                                errorMessage = result.message
                            )
                        }
                        result.message?.let { msg ->
                            _sideEffect.send(HomeSideEffect.ShowSnackbar(msg))
                        }
                    }
                }
            }
        }
    }
}
