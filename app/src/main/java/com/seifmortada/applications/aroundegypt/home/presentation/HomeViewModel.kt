package com.seifmortada.applications.aroundegypt.home.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.seifmortada.applications.aroundegypt.core.domain.Experience
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceRepository
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceResult
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel(private val experienceRepository: ExperienceRepository) : ViewModel() {

    private val _recommendedExperiencesState = MutableStateFlow<List<Experience>>(emptyList())
    val recommendedExperiencesState = _recommendedExperiencesState.asStateFlow()

    private val _recentExperiencesState = MutableStateFlow<List<Experience>>(emptyList())
    val recentExperiencesState = _recentExperiencesState.asStateFlow()

    private val _loadingState = MutableStateFlow(false)
    val loadingState = _loadingState.asStateFlow()

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState = _errorState.asStateFlow()

    init {
        fetchRecommendedExperiences()
        fetchRecentExperiences()
    }

    private fun fetchRecommendedExperiences() = viewModelScope.launch {
        experienceRepository.getRecommendedExperiences().collectLatest { response ->
            when (response) {
                is ExperienceResult.Error -> {
                    _loadingState.update { false }
                    _errorState.update { response.errorMessage.toString() }
                }

                is ExperienceResult.Loading -> _loadingState.update { true }
                is ExperienceResult.Success -> {
                    _loadingState.update { false }
                    _recommendedExperiencesState.update { response.data!! }
                }
            }
        }
    }

    private fun fetchRecentExperiences() = viewModelScope.launch {
        experienceRepository.getRecentExperiences().collectLatest { response ->
            when (response) {
                is ExperienceResult.Error -> {
                    _loadingState.update { false }
                    _errorState.update { response.errorMessage.toString() }
                }

                is ExperienceResult.Loading -> _loadingState.update { true }
                is ExperienceResult.Success -> {
                    _loadingState.update { false }
                    _recentExperiencesState.update { response.data!! }
                }
            }
        }
    }

    fun likeExperience(id: String) = viewModelScope.launch {
        val result = experienceRepository.likeExperience(id)
        when (result) {
            is ExperienceResult.Error -> {
                _errorState.update { result.errorMessage }
                _loadingState.update { false }
            }

            is ExperienceResult.Loading -> _loadingState.update { true }
            is ExperienceResult.Success -> {
                _loadingState.update { false }
                refresh()
            }
        }
    }

    private fun refresh() {
        fetchRecommendedExperiences()
        fetchRecentExperiences()
    }
}