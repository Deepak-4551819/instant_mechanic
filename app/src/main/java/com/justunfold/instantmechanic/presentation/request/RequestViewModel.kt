package com.justunfold.instantmechanic.presentation.request

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.justunfold.instantmechanic.data.repository.FirebaseRepository
import com.justunfold.instantmechanic.domain.model.ServiceRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

data class RequestUiState(
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val errorMessage: String? = null
)

@HiltViewModel
class RequestViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(RequestUiState())
    val uiState = _uiState.asStateFlow()

    fun submitBooking(
        mechanicId: String,
        mechanicName: String,
        customerName: String,
        phoneNumber: String,
        vehicleNumber: String,
        selectedService: String,
        description: String
    ) {
        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true, errorMessage = null) }
            val request = ServiceRequest(
                mechanicId = mechanicId,
                mechanicName = mechanicName,
                customerName = customerName,
                phoneNumber = phoneNumber,
                vehicleNumber = vehicleNumber,
                selectedService = selectedService,
                problemDescription = description
            )
            val result = firebaseRepository.submitServiceRequest(request)
            if (result.isSuccess) {
                _uiState.update { it.copy(isSubmitting = false, isSuccess = true) }
            } else {
                _uiState.update { 
                    it.copy(
                        isSubmitting = false, 
                        errorMessage = result.exceptionOrNull()?.localizedMessage ?: "Failed to submit request"
                    ) 
                }
            }
        }
    }
}
