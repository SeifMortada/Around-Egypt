package com.seifmortada.applications.aroundegypt.core.data.local

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ExperienceDao {

    @Query("SELECT * FROM Recommended_Experience_Table")
    suspend fun getRecommendedExperiences(): List<RecommendedExperienceEntity>

    @Query("SELECT * FROM Recent_Experience_Table")
    suspend fun getRecentExperiences(): List<RecentExperiencesEntity>

    @Upsert
    suspend fun upsertRecommendedExperiences(experiences: List<RecommendedExperienceEntity>)

    @Upsert
    suspend fun upsertRecentExperiences(experiences: List<RecentExperiencesEntity>)

    @Query("UPDATE Recommended_Experience_Table SET numberOfLikes =:newLikesCount,isLiked =:newIsLiked ,numberOfViews =numberOfViews + 1 WHERE experienceId = :id")
    suspend fun updateRecommendedExperience(id: String, newLikesCount: String,newIsLiked: Boolean)

    @Query("UPDATE Recent_Experience_Table SET numberOfLikes =:newLikesCount,isLiked =:newIsLiked,numberOfViews =numberOfViews + 1 WHERE experienceId = :id")
    suspend fun updateRecentExperience(id: String, newLikesCount: String,newIsLiked: Boolean)

}