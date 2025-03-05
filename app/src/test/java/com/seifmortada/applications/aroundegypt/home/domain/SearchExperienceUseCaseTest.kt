package com.seifmortada.applications.aroundegypt.home.domain


import com.seifmortada.applications.aroundegypt.MainCoroutineRule
import com.seifmortada.applications.aroundegypt.core.domain.*
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class SearchExperienceUseCaseTest {

    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()
    private lateinit var searchExperienceUseCase: SearchExperienceUseCase
    private val repository: ExperienceRepository = mockk()

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

    @Before
    fun setup() {
        searchExperienceUseCase = SearchExperienceUseCase(repository)
    }

    @Test
    fun `invoke should emit Loading first`() = runTest {
        coEvery { repository.searchExperience("Giza") } returns experiences

        val result = searchExperienceUseCase("Giza").first()

        assertTrue(result is ExperienceResult.Loading)
    }

    @Test
    fun `invoke should return error when query is blank`() = runTest {
        val result = searchExperienceUseCase("").last()
        assertTrue(result is ExperienceResult.Error)
        assertEquals("Blank", (result as ExperienceResult.Error).errorMessage)
    }

    @Test
    fun `invoke should return success with experiences`() = runTest {
        coEvery { repository.searchExperience("Giza") } returns experiences

        val result = searchExperienceUseCase("Giza").last()

        assertTrue(result is ExperienceResult.Success)
        assertEquals(2, (result as ExperienceResult.Success).data?.size)
    }

    @Test
    fun `invoke should return error when repository throws exception`() = runTest {

        coEvery { repository.searchExperience("Giza") } throws Exception("Network Error")

        val result = searchExperienceUseCase("Giza").last()

        assertTrue(result is ExperienceResult.Error)
        assertEquals(
            "Blank",
            (result as ExperienceResult.Error).errorMessage
        )
    }
}
