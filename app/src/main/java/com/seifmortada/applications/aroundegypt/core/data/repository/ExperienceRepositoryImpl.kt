package com.seifmortada.applications.aroundegypt.core.data.repository

import com.seifmortada.applications.aroundegypt.core.data.local.ExperienceDao
import com.seifmortada.applications.aroundegypt.core.data.mappers.toExperience
import com.seifmortada.applications.aroundegypt.core.data.mappers.toExperienceEntity
import com.seifmortada.applications.aroundegypt.core.data.network.api.ExperienceService
import com.seifmortada.applications.aroundegypt.core.domain.Experience
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceRepository
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class ExperienceRepositoryImpl(
    private val experienceService: ExperienceService,
    private val experienceDao: ExperienceDao
) : ExperienceRepository {

    override fun getRecommendedExperiences(): Flow<ExperienceResult<List<Experience>>> =
        flow {
            emit(ExperienceResult.Loading())
            val cachedExperiences =
                experienceDao.getRecommendedExperiences().map { it.toExperience() }
            if (cachedExperiences.isNotEmpty()) {
                emit(ExperienceResult.Success(cachedExperiences))
            }
            val response = experienceService.getRecommendedExperiences()
            if (response.isSuccessful && response.body() != null) {
                val experiences = response.body()!!.data.map { it.toExperience() }
                experienceDao.upsertExperiences(experiences.map { it.toExperienceEntity() })
                emit(ExperienceResult.Success(experiences))
            } else {
                emit(ExperienceResult.Error("Failed to fetch experiences"))
            }
        }.catch { e -> emit(ExperienceResult.Error(e.message ?: "Unknown error")) }

    override fun getRecentExperiences(): Flow<ExperienceResult<List<Experience>>> =
        flow {
            emit(ExperienceResult.Loading())
            val cachedExperiences =
                experienceDao.getRecommendedExperiences().map { it.toExperience() }
            if (cachedExperiences.isNotEmpty()) {
                emit(ExperienceResult.Success(cachedExperiences))
            }
            val response = experienceService.getRecentExperiences()
            if (response.isSuccessful && response.body() != null) {
                val experiences = response.body()!!.data.map { it.toExperience() }
                experienceDao.upsertExperiences(experiences.map { it.toExperienceEntity() })
                emit(ExperienceResult.Success(experiences))
            } else {
                emit(ExperienceResult.Error("Failed to fetch recent experiences"))
            }
        }.catch { e -> emit(ExperienceResult.Error(e.message ?: "Unknown error")) }


    override fun searchExperience(query: String): Flow<ExperienceResult<List<Experience>>> =
        flow {
            emit(ExperienceResult.Loading())
            val response = experienceService.searchExperiences(query)
            if (response.isSuccessful && response.body() != null) {
                val experiences = response.body()!!.data.map { it.toExperience() }
                emit(ExperienceResult.Success(experiences))
            } else {
                emit(ExperienceResult.Error("Failed to fetch search results"))
            }
        }.catch { e -> emit(ExperienceResult.Error(e.message ?: "Unknown error")) }


    override suspend fun getSingleExperience(id: String): ExperienceResult<Experience> {
        return try {
            val response = experienceService.getSingleExperience(id)
            if (response.isSuccessful && response.body() != null) {
                ExperienceResult.Success(response.body()!!.data.map { it.toExperience() }[0])
            } else {
                ExperienceResult.Error("Failed to fetch experience")
            }
        } catch (e: Exception) {
            ExperienceResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun likeExperience(id: String): ExperienceResult<List<Experience>> {
        return try {
            val response = experienceService.likeExperience(id)
            if (response.isSuccessful && response.body() != null) {
                ExperienceResult.Success(response.body()!!.data.map { it.toExperience() })
            } else {
                ExperienceResult.Error("Failed to like experience")
            }
        } catch (e: Exception) {
            ExperienceResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun upsertExperiences(experiences: List<Experience>) {
        experienceDao.upsertExperiences(experiences.map { it.toExperienceEntity() })
    }

    override suspend fun clearExperiences() {
        experienceDao.clearExperiences()
    }
}