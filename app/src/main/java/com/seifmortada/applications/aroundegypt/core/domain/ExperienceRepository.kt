package com.seifmortada.applications.aroundegypt.core.domain

import kotlinx.coroutines.flow.Flow

interface ExperienceRepository {
    fun getRecommendedExperiences(): Flow<ExperienceResult<List<Experience>>>
    fun getRecentExperiences(): Flow<ExperienceResult<List<Experience>>>
   suspend fun searchExperience(query: String): List<Experience>
    suspend fun getSingleExperience(id: String): ExperienceResult<Experience>
    suspend fun likeExperience(id: String): ExperienceResult<Boolean>
    suspend fun upsertRecentExperiences(experiences: List<Experience>)
    suspend fun upsertRecommendedExperiences(experiences: List<Experience>)
    suspend fun clearRecentExperiences()
    suspend fun clearRecommendedExperiences()
}