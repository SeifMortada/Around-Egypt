package com.seifmortada.applications.aroundegypt.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ExperienceDao {

    @Query("SELECT * FROM Experience_Table")
    suspend fun getRecommendedExperiences(): List<ExperienceEntity>

    @Upsert
    suspend fun upsertExperiences(experiences: List<ExperienceEntity>)

    @Query("SELECT * FROM Experience_Table WHERE experienceId = :id")
    suspend fun getSingleExperience(id: String): ExperienceEntity

    @Query("DELETE FROM Experience_Table")
    suspend fun clearExperiences()

}