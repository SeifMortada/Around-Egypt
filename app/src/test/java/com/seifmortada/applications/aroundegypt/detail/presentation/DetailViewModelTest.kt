package com.seifmortada.applications.aroundegypt.detail.presentation

import org.junit.Assert.*

import app.cash.turbine.test
import com.seifmortada.applications.aroundegypt.MainCoroutineRule
import com.seifmortada.applications.aroundegypt.core.domain.Experience
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceRepository
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceResult
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class DetailViewModelTest {

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var viewModel: DetailViewModel
    private val repository: ExperienceRepository = mockk()

    private val experience =   Experience(
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

    @Before
    fun setUp() {
        viewModel = DetailViewModel(repository)
    }

    @Test
    fun `getExperience should update experience on success`() = runTest {
        coEvery { repository.getSingleExperience("1") } returns ExperienceResult.Success(experience)

        viewModel.getExperience("1")

        viewModel.experience.test {
            assertEquals(experience, awaitItem())
        }
    }

    @Test
    fun `getExperience should update error state on failure`() = runTest {
        coEvery { repository.getSingleExperience("1") } returns ExperienceResult.Error("Failed")

        viewModel.getExperience("1")

        viewModel.errorState.test {
            assertEquals("Failed", awaitItem())
        }
    }

    @Test
    fun `likeExperience should update error state on failure`() = runTest {
        coEvery { repository.likeExperience("1") } returns ExperienceResult.Error("Failed to like")

        viewModel.likeExperience("1")

        viewModel.errorState.test {
            assertEquals("Failed to like", awaitItem())
        }
    }

    @Test
    fun `likeExperience should handle success case`() = runTest {
        coEvery { repository.likeExperience("1") } returns ExperienceResult.Success(true)

        viewModel.likeExperience("1")

        viewModel.loadingState.test {
            assertEquals(false, awaitItem())
        }
    }
}
