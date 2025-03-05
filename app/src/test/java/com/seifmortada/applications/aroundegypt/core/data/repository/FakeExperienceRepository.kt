package com.seifmortada.applications.aroundegypt.core.data.repository

import com.seifmortada.applications.aroundegypt.core.domain.Experience
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceRepository
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow

class FakeExperienceRepository : ExperienceRepository {
    private val fakeExperiences = mutableListOf(
        Experience(
            id = "1",
            title = "Giza Pyramids",
            description = "Ancient wonder of the world",
            imgSrc = "https://example.com/pyramids.jpg",
            numberOfViews = 5000,
            numberOfLikes = 1200,
            recommended = 0,
            isLiked = false,
            address = "Giza, Egypt"
        ),
        Experience(
            id = "2",
            title = "Luxor Temple",
            description = "Historic temple in Luxor",
            imgSrc = "https://example.com/luxor.jpg",
            numberOfViews = 3000,
            numberOfLikes = 900,
            recommended = 1,
            isLiked = true,
            address = "Luxor, Egypt"
        )
    )

    override fun getRecommendedExperiences(): Flow<ExperienceResult<List<Experience>>> = flow {
        emit(ExperienceResult.Loading())
        val recommendedExperiences = fakeExperiences.filter { it.recommended == 1 }
        emit(ExperienceResult.Success(recommendedExperiences))
    }.catch { e -> emit(ExperienceResult.Error(e.message ?: "Unknown error")) }

    override fun getRecentExperiences(): Flow<ExperienceResult<List<Experience>>> = flow {
        emit(ExperienceResult.Loading())
        val recentExperiences = fakeExperiences.sortedByDescending { it.numberOfViews } // Ensure sorting
        emit(ExperienceResult.Success(recentExperiences))
    }.catch { e -> emit(ExperienceResult.Error(e.message ?: "Unknown error")) }


    override suspend fun searchExperience(query: String): List<Experience> {
        return fakeExperiences.filter { it.title.contains(query, ignoreCase = true) || it.address.contains(query, ignoreCase = true) }
    }

    override suspend fun getSingleExperience(id: String): ExperienceResult<Experience> {
        return fakeExperiences.find { it.id == id }
            ?.let { ExperienceResult.Success(it) }
            ?: ExperienceResult.Error("Experience not found")
    }

    override suspend fun likeExperience(id: String): ExperienceResult<Boolean> {
        val experience = fakeExperiences.find { it.id == id }
        return if (experience != null) {
            val updatedExperience = experience.copy(isLiked = !experience.isLiked, numberOfLikes = experience.numberOfLikes + if (!experience.isLiked) 1 else -1)
            fakeExperiences[fakeExperiences.indexOf(experience)] = updatedExperience
            ExperienceResult.Success(true)
        } else {
            ExperienceResult.Error("Experience not found")
        }
    }
}
