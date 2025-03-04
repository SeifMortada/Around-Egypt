package com.seifmortada.applications.aroundegypt.detail.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.aroundegypt.core.domain.Experience
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceRepository
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: ExperienceRepository) : ViewModel() {

    private val _experience = MutableStateFlow<Experience?>(null)
    val experience = _experience.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    fun getExperience(experienceId: String) {
        viewModelScope.launch {
            _loadingState.update { true }
            when (val response = repository.getSingleExperience(experienceId)) {
                is ExperienceResult.Error -> {
                    _loadingState.update { false }
                    _errorState.update { response.errorMessage }
                }

                is ExperienceResult.Loading -> _loadingState.update { true }
                is ExperienceResult.Success -> {
                    _loadingState.update { false }
                    _experience.update { response.data }
                }
            }

        }
    }
    fun likeExperience(id: String) = viewModelScope.launch {
        when (val result = repository.likeExperience(id)) {
            is ExperienceResult.Error -> {
                _errorState.update { result.errorMessage }
                _loadingState.update { false }
            }

            is ExperienceResult.Loading -> _loadingState.update { true }
            is ExperienceResult.Success -> {
                _loadingState.update { false }
            }
        }
    }
}