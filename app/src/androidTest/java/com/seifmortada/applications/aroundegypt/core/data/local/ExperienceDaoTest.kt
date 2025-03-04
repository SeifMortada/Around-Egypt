package com.seifmortada.applications.aroundegypt.core.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@SmallTest
class ExperienceDaoTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var database: ExperienceDatabase
    private lateinit var dao: ExperienceDao
    val recommendedExperiences = listOf(
        RecommendedExperienceEntity(
            "1", "Title 1", "desc 1", "URL1",
            10, 5, false, 1, "Address 1"
        ))
    val recentExperiences = listOf(
        RecentExperiencesEntity(
            "1", "Title 1", "desc 1", "URL1",
            10, 5, 1, false, "Address 1"
        )
        )

    @Before
    fun setup() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            ExperienceDatabase::class.java
        ).allowMainThreadQueries().build()

        dao = database.dao
    }

    @After
    fun teardown() {
        database.close()
    }

    @Test
    fun insertAndRetrieveRecommendedExperiences() = runTest {
        dao.upsertRecommendedExperiences(recommendedExperiences)

        val retrieved = dao.getRecommendedExperiences()
        assertThat(retrieved).hasSize(1)
        assertThat(retrieved).containsExactlyElementsIn(recommendedExperiences)
    }

    @Test
    fun insertAndRetrieveRecentExperiences() = runTest {
        dao.upsertRecentExperiences(recentExperiences)

        val retrieved = dao.getRecentExperiences()
        assertThat(retrieved).hasSize(1)
        assertThat(retrieved).containsExactlyElementsIn(recentExperiences)
    }

    @Test
    fun updateRecommendedExperience() = runTest {
        dao.upsertRecommendedExperiences(recommendedExperiences)

        dao.updateRecommendedExperience("1", "20", true)

        val updated = dao.getRecommendedExperiences().first { it.experienceId == "1" }
        assertThat(updated.numberOfLikes).isEqualTo(20)
        assertThat(updated.isLiked).isTrue()
        assertThat(updated.numberOfViews).isEqualTo(11) // Should be incremented by 1
    }

    @Test
    fun updateRecentExperience() = runTest {
        dao.upsertRecentExperiences(recentExperiences)

        dao.updateRecentExperience("1", "18", true)

        val updated = dao.getRecentExperiences().first { it.experienceId == "1" }
        assertThat(updated.numberOfLikes).isEqualTo(18)
        assertThat(updated.isLiked).isTrue()
        assertThat(updated.numberOfViews).isEqualTo(11) // Should be incremented by 1
    }
}
