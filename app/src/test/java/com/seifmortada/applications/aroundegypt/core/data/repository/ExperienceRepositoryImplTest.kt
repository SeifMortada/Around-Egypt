package com.seifmortada.applications.aroundegypt.core.data.repository

import com.seifmortada.applications.aroundegypt.MainCoroutineRule
import com.seifmortada.applications.aroundegypt.core.domain.ExperienceResult
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FakeExperienceRepositoryTest {

    private lateinit var repository: FakeExperienceRepository

    @get:Rule
    private val mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setup() {
        repository = FakeExperienceRepository()
    }

    @Test
    fun `getRecommendedExperiences should return list of recommended experiences`() = runTest {
        val result = repository.getRecommendedExperiences().last()

        assertTrue(result is ExperienceResult.Success)
        assertEquals(1, (result as ExperienceResult.Success).data?.size)
    }


    @Test
    fun `getRecentExperiences should return sorted list by views`() = runTest {
        val result = repository.getRecentExperiences().last()

        assertTrue(result is ExperienceResult.Success)
        val experiences = (result as ExperienceResult.Success).data!!

        assertEquals("Giza Pyramids", experiences[0].title) // Highest views: 5000
        assertEquals("Luxor Temple", experiences[1].title) // Lower views: 3000
    }

    @Test
    fun `searchExperience should return matching experiences`() = runTest {
        val result = repository.searchExperience("Luxor")

        assertEquals(1, result.size)
        assertEquals("Luxor Temple", result.first().title)
    }

    @Test
    fun `searchExperience should return empty list when no match`() = runTest {
        val result = repository.searchExperience("Unknown Place")
        assertTrue(result.isEmpty())
    }

    @Test
    fun `getSingleExperience should return experience when id exists`() = runTest {
        val result = repository.getSingleExperience("1")

        assertTrue(result is ExperienceResult.Success)
        assertEquals("Giza Pyramids", (result as ExperienceResult.Success).data?.title)
    }

    @Test
    fun `getSingleExperience should return error when id does not exist`() = runTest {
        val result = repository.getSingleExperience("99")
        assertTrue(result is ExperienceResult.Error)
    }

    @Test
    fun `likeExperience should toggle like state and update like count`() = runTest {
        val beforeLike = repository.getSingleExperience("1") as ExperienceResult.Success
        val beforeLikes = beforeLike.data?.numberOfLikes
        val beforeLiked = beforeLike.data?.isLiked

        repository.likeExperience("1")

        val afterLike = repository.getSingleExperience("1") as ExperienceResult.Success
        val afterLikes = afterLike.data?.numberOfLikes
        val afterLiked = afterLike.data?.isLiked

        assertEquals(beforeLikes?.inc(), afterLikes)
        assertNotEquals(beforeLiked, afterLiked)
    }

    @Test
    fun `likeExperience should return error when id does not exist`() = runTest {

        val result = repository.likeExperience("99")
        assertTrue(result is ExperienceResult.Error)
    }
}
