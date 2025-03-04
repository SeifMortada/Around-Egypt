package com.seifmortada.applications.aroundegypt.home.domain

import com.seifmortada.applications.aroundegypt.core.domain.Experience
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceRepository
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class SearchExperienceUseCase(private val repository: ExperienceRepository) {
    suspend operator fun invoke(query: String): Flow<ExperienceResult<List<Experience>>> {
        return flow {
            emit(ExperienceResult.Loading())

            if (query.isBlank()) {
                emit(ExperienceResult.Error("Blank"))
                return@flow
            }
            val experience =
                try {
                    repository.searchExperience(query)
                } catch (e: Exception) {
                    e.printStackTrace()
                    emit(ExperienceResult.Error("Blank"))
                    return@flow
                }
            experience.let {
                emit(ExperienceResult.Success(it))
                return@flow
            }
        }
    }
}