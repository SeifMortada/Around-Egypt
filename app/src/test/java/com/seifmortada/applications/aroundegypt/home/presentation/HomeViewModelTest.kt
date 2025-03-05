package com.seifmortada.applications.aroundegypt.home.presentation

import com.seifmortada.applications.aroundegypt.MainCoroutineRule
import com.seifmortada.applications.aroundegypt.core.domain.*
import com.seifmortada.applications.aroundegypt.home.domain.SearchExperienceUseCase
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.*
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    @get:Rule
    val mainCoroutineRule=MainCoroutineRule()

    private lateinit var viewModel: HomeViewModel
    private val repository: ExperienceRepository = mockk()
    private val searchUseCase: SearchExperienceUseCase = mockk()

    private val experiences = listOf(
        Experience(
            id = "1",
            title = "Giza Pyramids",
            description = "Ancient wonder of the world",
            imgSrc = "https://example.com/pyramids.jpg",
            numberOfViews = 5000,
            numberOfLikes = 1200,
            recommended = 1,
            isLiked = false,
            address = "Giza, Egypt"
        )
    )

    @Before
    fun setup() {
        coEvery { repository.getRecommendedExperiences() } returns flow {
            emit(ExperienceResult.Loading())
            delay(100)
            emit(ExperienceResult.Success(experiences))
        }

        coEvery { repository.getRecentExperiences() } returns flow {
            emit(ExperienceResult.Loading())
            delay(100)
            emit(ExperienceResult.Success(experiences))
        }

        viewModel = HomeViewModel(repository, searchUseCase)
    }

    @Test
    fun `fetchRecommendedExperiences should update recommendedExperiencesState on success`() = runTest {
        advanceUntilIdle()
        assertEquals(experiences, viewModel.recommendedExperiencesState.value)
    }

    @Test
    fun `fetchRecommendedExperiences should update errorState on failure`() = runTest {
        coEvery { repository.getRecommendedExperiences() } returns flow {
            emit(ExperienceResult.Error("Failed to fetch"))
        }

        viewModel = HomeViewModel(repository, searchUseCase)
        advanceUntilIdle()
        assertEquals("Failed to fetch", viewModel.errorState.value)
    }

    @Test
    fun `fetchRecentExperiences should update recentExperiencesState on success`() = runTest {
        advanceUntilIdle()
        assertEquals(experiences, viewModel.recentExperiencesState.value)
    }

    @Test
    fun `fetchRecentExperiences should update errorState on failure`() = runTest {
        coEvery { repository.getRecentExperiences() } returns flow {
            emit(ExperienceResult.Error("Failed to fetch"))
        }

        viewModel = HomeViewModel(repository, searchUseCase)
        advanceUntilIdle()
        assertEquals("Failed to fetch", viewModel.errorState.value)
    }

    @Test
    fun `searchExperiences should update searchExperiencesState on success`() = runTest {
        coEvery { searchUseCase("Giza") } returns flow {
            emit(ExperienceResult.Loading())
            emit(ExperienceResult.Success(experiences))
        }

        viewModel.searchExperiences("Giza")
        advanceUntilIdle()

        assertEquals(experiences, viewModel.searchExperiencesState.value)
    }

    @Test
    fun `searchExperiences should update errorState on failure`() = runTest {
        coEvery { searchUseCase("Giza") } returns flow {
            emit(ExperienceResult.Error("Search failed"))
        }

        viewModel.searchExperiences("Giza")
        advanceUntilIdle()

        assertEquals("Search failed", viewModel.errorState.value)
    }

    @Test
    fun `likeExperience should refresh data on success`() = runTest {
        coEvery { repository.likeExperience("1") } returns ExperienceResult.Success(true)
        coEvery { repository.getRecommendedExperiences() } returns flow { emit(ExperienceResult.Success(experiences)) }
        coEvery { repository.getRecentExperiences() } returns flow { emit(ExperienceResult.Success(experiences)) }

        viewModel.likeExperience("1")
        advanceUntilIdle()

        coVerify(atLeast = 1) { repository.getRecommendedExperiences() }
        coVerify(atLeast = 1) { repository.getRecentExperiences() }
    }

    @Test
    fun `likeExperience should update errorState on failure`() = runTest {
        coEvery { repository.likeExperience("1") } returns ExperienceResult.Error("Like failed")

        viewModel.likeExperience("1")
        advanceUntilIdle()

        assertEquals("Like failed", viewModel.errorState.value)
    }
}
