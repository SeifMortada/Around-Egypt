package com.seifmortada.applications.aroundegypt.core.data.repository

import com.seifmortada.applications.aroundegypt.core.data.local.ExperienceDao
import com.seifmortada.applications.aroundegypt.core.data.mappers.toExperience
import com.seifmortada.applications.aroundegypt.core.data.mappers.toRecentExperiencesEntity
import com.seifmortada.applications.aroundegypt.core.data.mappers.toRecommendedExperienceEntity
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
                return@flow
            }
            val response = experienceService.getRecommendedExperiences()
            if (response.isSuccessful && response.body() != null) {
                val experiences = response.body()!!.data
                    .filter { it.recommended == 1 }
                    .map { it.toExperience() }
                experienceDao.upsertRecommendedExperiences(experiences.map { it.toRecommendedExperienceEntity() })
                emit(ExperienceResult.Success(experiences))
            } else {
                emit(ExperienceResult.Error("Failed to fetch experiences"))
            }
        }.catch { e -> emit(ExperienceResult.Error(e.message ?: "Unknown error")) }

    override fun getRecentExperiences(): Flow<ExperienceResult<List<Experience>>> =
        flow {
            emit(ExperienceResult.Loading())
            val cachedExperiences =
                experienceDao.getRecentExperiences().map { it.toExperience() }
            if (cachedExperiences.isNotEmpty()) {
                emit(ExperienceResult.Success(cachedExperiences))
                return@flow
            }
            val response = experienceService.getRecentExperiences()
            if (response.isSuccessful && response.body() != null) {
                val experiences = response.body()!!.data.map { it.toExperience() }
                experienceDao.upsertRecentExperiences(experiences.map { it.toRecentExperiencesEntity() })
                emit(ExperienceResult.Success(experiences))
            } else {
                emit(ExperienceResult.Error("Failed to fetch recent experiences"))
            }
        }.catch { e -> emit(ExperienceResult.Error(e.message ?: "Unknown error")) }


    override suspend fun searchExperience(query: String): List<Experience> {
        val response = experienceService.searchExperiences(query)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!.data.map { it.toExperience() }
        }
        return emptyList()
    }


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

    override suspend fun likeExperience(id: String): ExperienceResult<Boolean> {
        return try {
            val response = experienceService.likeExperience(id)
            if (response.isSuccessful && response.body() != null) {
                if (response.body()!!.meta.code == 200) {
                    if (experienceDao.getRecommendedExperiences().any { it.experienceId == id }) {
                        experienceDao.updateRecommendedExperience(
                            id,
                            response.body()!!.newLikesCount.toString(),
                            true
                        )
                    } else {
                        experienceDao.updateRecentExperience(
                            id,
                            response.body()!!.newLikesCount.toString(),
                            true
                        )
                    }
                    ExperienceResult.Success(true)
                } else ExperienceResult.Error(response.body()!!.meta.errors[0].message)
            } else {
                ExperienceResult.Error("Failed to like experience")
            }
        } catch (e: Exception) {
            ExperienceResult.Error(e.message ?: "Unknown error")
        }
    }

    override suspend fun upsertRecentExperiences(experiences: List<Experience>) {
        experienceDao.upsertRecentExperiences(experiences.map { it.toRecentExperiencesEntity() })
    }

    override suspend fun upsertRecommendedExperiences(experiences: List<Experience>) {
        experienceDao.upsertRecommendedExperiences(experiences.map { it.toRecommendedExperienceEntity() })
    }

    override suspend fun clearRecentExperiences() {
        experienceDao.clearRecentExperiences()
    }

    override suspend fun clearRecommendedExperiences() {
        experienceDao.clearRecommendedExperiences()
    }
}